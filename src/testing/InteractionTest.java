package testing;

import java.io.IOException;

import org.junit.Test;

import app_kvEcs.ECSClient;
import client.KVStore;
import common.messages.KVMessage;
import common.messages.StatusType;
import junit.framework.TestCase;

public class InteractionTest extends TestCase {

    private KVStore kvClient;
    private ECSClient ecsClient;

    public void setUp() throws IOException {
	ecsClient = new ECSClient();
	kvClient = new KVStore("localhost", 50000);
	try {
	    ecsClient.initService(5, 4, "FIFO");
	    ecsClient.start();
	    kvClient.connect();
	} catch (Exception e) {
	}
    }

    public void tearDown() throws Exception {
	kvClient.disconnect();
    }

    @Test
    public void testPut() {
	String key = "foo";
	String value = "bar";
	KVMessage response = null;
	Exception ex = null;

	try {
	    response = kvClient.put(key, null);
	    response = kvClient.put(key, value);
	} catch (Exception e) {
	    ex = e;
	}

	assertTrue(ex == null && response.getStatus() == StatusType.PUT_SUCCESS);
    }

    @Test
    public void testPutDisconnected() throws Exception {
	kvClient.disconnect();
	String key = "foo";
	String value = "bar";
	Exception ex = null;

	try {
	    kvClient.put(key, value);
	} catch (Exception e) {
	    ex = e;
	}

	assertNotNull(ex);
    }

    @Test
    public void testUpdate() {
	String key = "updateTestValue";
	String initialValue = "initial";
	String updatedValue = "updated";

	KVMessage response = null;
	Exception ex = null;

	try {
	    kvClient.put(key, initialValue);
	    response = kvClient.put(key, updatedValue);

	} catch (Exception e) {
	    ex = e;
	}

	assertTrue(ex == null && response.getStatus() == StatusType.PUT_UPDATE
		&& response.getValue().equals(updatedValue));
    }

    @Test
    public void testDelete() {
	String key = "deleteTestValue";
	String value = "toDelete";

	KVMessage response = null;
	Exception ex = null;

	try {
	    kvClient.put(key, value);
	    response = kvClient.put(key, null);

	} catch (Exception e) {
	    ex = e;
	}

	assertTrue(ex == null && response.getStatus() == StatusType.DELETE_SUCCESS);
    }

    @Test
    public void testGet() {
	String key = "foo";
	String value = "bar";
	KVMessage response = null;
	Exception ex = null;

	try {
	    kvClient.put(key, value);
	    response = kvClient.get(key);
	} catch (Exception e) {
	    ex = e;
	}

	assertTrue(ex == null && response.getValue().equals("bar"));
    }

    @Test
    public void testGetUnsetValue() {
	String key = "an unset value";
	KVMessage response = null;
	Exception ex = null;

	try {
	    response = kvClient.get(key);
	} catch (Exception e) {
	    ex = e;
	}

	assertTrue(ex == null && response.getStatus() == StatusType.GET_ERROR);
    }

}