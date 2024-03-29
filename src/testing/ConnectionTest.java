package testing;

import java.net.UnknownHostException;

import client.KVStore;
import junit.framework.TestCase;

public class ConnectionTest extends TestCase {

    public void testConnectionSuccess() {

	Exception ex = null;

	try {
	    KVStore kvClient = new KVStore("localhost", 50000);
	    kvClient.connect();
	} catch (Exception e) {
	    ex = e;
	}

	assertNull(ex);
    }

    public void testUnknownHost() {
	Exception ex = null;

	try {
	    KVStore kvClient = new KVStore("unknown", 50000);
	    kvClient.connect();
	} catch (Exception e) {
	    ex = e;
	}

	assertTrue(ex instanceof UnknownHostException);
    }

    public void testIllegalPort() {
	Exception ex = null;

	try {
	    KVStore kvClient = new KVStore("localhost", 123456789);
	    kvClient.connect();
	} catch (Exception e) {
	    ex = e;
	}

	assertTrue(ex instanceof IllegalArgumentException);
    }

}
