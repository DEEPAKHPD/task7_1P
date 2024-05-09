package sit707_week7;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;


public class BodyTemperatureMonitorTest {

    @Mock
    private TemperatureSensor temperatureSensor;
    
    @Mock
    private NotificationSender notificationSender;
    
    @Mock
    private CloudService cloudService;

    private BodyTemperatureMonitor monitor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);  
        monitor = new BodyTemperatureMonitor(temperatureSensor, cloudService, notificationSender);
    }

	@Test
	public void testStudentIdentity() {
		String studentId = "s222421064";
		Assert.assertNotNull("Student ID is null", studentId);
	}

	@Test
	public void testStudentName() {
		String studentName = "Deepak Damodaran";
		Assert.assertNotNull("Student name is null", studentName);
	}
	
	@Test
	public void testReadTemperatureNegative() {
	    
	    when(temperatureSensor.readTemperatureValue()).thenReturn(-5.0);
	    double result = monitor.readTemperature();
	    Assert.assertEquals("Temperature is negative", -5.0, result, 0.0);
	}

	
	@Test
	public void testReadTemperatureZero() {
	    
	    when(temperatureSensor.readTemperatureValue()).thenReturn(0.0);
	    double result = monitor.readTemperature();
	    Assert.assertEquals("Temperature should be zero", 0.0, result, 0.0);
	}

	
	@Test
	public void testReadTemperatureNormal() {
	    
	    when(temperatureSensor.readTemperatureValue()).thenReturn(36.6);
	    double result = monitor.readTemperature();
	    Assert.assertEquals("Temperature should be normal", 36.6, result, 0.0);
	}


	@Test
	public void testReadTemperatureAbnormallyHigh() {
	    
	    when(temperatureSensor.readTemperatureValue()).thenReturn(39.0);
	    double result = monitor.readTemperature();
	    Assert.assertTrue("Temperature should be abnormally high", result > 37.0);
	}
	
	
	
	@Test
	public void testReadTemperatureVeryHigh() {
	    when(temperatureSensor.readTemperatureValue()).thenReturn(41.0); 
	    double result = monitor.readTemperature();
	    Assert.assertTrue("Temperature should trigger emergency protocol", result > 40.0);
	}


	/*
	 * CREDIT or above level students, Remove comments. 
	 */
	@Test
	public void testReportTemperatureReadingToCloud() {
	    
	    TemperatureReading tempReading = new TemperatureReading();
	    tempReading.setTemperature(36.5); // TemperatureReading class has a method to set temperature
	    monitor.reportTemperatureReadingToCloud(tempReading);
	    verify(cloudService).sendTemperatureToCloud(tempReading);
	}

	
	/*
	 * CREDIT or above level students, Remove comments. 
	 */
	@Test
	public void testInquireBodyStatusNormalNotification() {
	    
	    when(cloudService.queryCustomerBodyStatus(any(Customer.class))).thenReturn("NORMAL");
	    monitor.inquireBodyStatus();
	    verify(notificationSender).sendEmailNotification(any(Customer.class), eq("Thumbs Up!"));
	}

	
	/*
	 * CREDIT or above level students, Remove comments. 
	 */
	@Test
	public void testInquireBodyStatusAbnormalNotification() {
	    when(cloudService.queryCustomerBodyStatus(any(Customer.class))).thenReturn("ABNORMAL");
	    monitor.inquireBodyStatus();
	    verify(notificationSender).sendEmailNotification(any(FamilyDoctor.class), eq("Emergency!"));
	}

}