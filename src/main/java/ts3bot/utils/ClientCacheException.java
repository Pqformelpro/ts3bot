package main.java.ts3bot.utils;

public class ClientCacheException extends Exception {

	private static final long serialVersionUID = -1775264043737159375L;
	
	public ClientCacheException() {
		
	}
	
	public ClientCacheException(String message) {
		super(message);
	}
	
	public ClientCacheException(Throwable cause) {
        super(cause);
    }

    public ClientCacheException(String message, Throwable cause) {
        super(message, cause);
    }

}
