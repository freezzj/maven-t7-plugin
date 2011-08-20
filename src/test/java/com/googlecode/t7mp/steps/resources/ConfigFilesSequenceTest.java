package com.googlecode.t7mp.steps.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.SysoutLog;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.DefaultContext;

public class ConfigFilesSequenceTest {
	
    private File catalinaBaseDir;
    private File confDirectory;
    private AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
    private Log log = new SysoutLog();
    
    @Before
    public void setUp(){
    	File tempDir = new File(System.getProperty("java.io.tmpdir"));
    	catalinaBaseDir = new File(tempDir, UUID.randomUUID().toString());
    	boolean created = catalinaBaseDir.mkdirs();
    	Assert.assertTrue(created);
    	Assert.assertNotNull(catalinaBaseDir);
    	Assert.assertTrue(catalinaBaseDir.exists());
    	confDirectory = new File(catalinaBaseDir, "/conf/");
    	boolean confDirCreated = confDirectory.mkdirs();
    	Assert.assertTrue(confDirCreated);
    	Assert.assertNotNull(confDirectory);
    	Assert.assertTrue(confDirectory.exists());
    }
    
    @After
    public void tearDown() throws IOException{
    	FileUtils.deleteDirectory(catalinaBaseDir);
    }
    
    @Test
    public void testConfigFilesSequence() throws IOException{
    	Mockito.when(mojo.getCatalinaBase()).thenReturn(catalinaBaseDir);
    	Mockito.when(mojo.getLog()).thenReturn(log);
    	Context context = new DefaultContext(mojo);
    	ConfigFilesSequence sequence = new ConfigFilesSequence();
    	sequence.execute(context);
    	File catalinaProperties = new File(confDirectory, "catalina.properties");
    	Assert.assertTrue(catalinaProperties.exists());
    	System.out.println(new String(getBytesFromFile(catalinaProperties)));
    }
    
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }


}
