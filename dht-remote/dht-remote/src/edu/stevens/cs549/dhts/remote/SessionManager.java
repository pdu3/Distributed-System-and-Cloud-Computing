package edu.stevens.cs549.dhts.remote;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

import edu.stevens.cs549.dhts.main.IShell;
import edu.stevens.cs549.dhts.main.LocalShell;

/**
 * Maintain a stack of shells.
 * @author dduggan
 *
 */
public class SessionManager {
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SessionManager.class.getCanonicalName());
	
	public static final String ACK = "ACK";
	
	private static final SessionManager SESSION_MANAGER = new SessionManager();
	
	private static final ShellManager SHELL_MANAGER = ShellManager.getShellManager();
	
	public static SessionManager getSessionManager() {
		return SESSION_MANAGER;
	}
	
	private Lock lock = new ReentrantLock();
	
	private ControllerServer currentServer;
	
	public boolean isSession() {
		return currentServer != null;
	}

	public Session getCurrentSession() {
		return currentServer != null ? currentServer.getSession() : null;
	}

	public boolean setCurrentSession(ControllerServer server) {
		lock.lock();
		try {
			if (currentServer == null) {
				currentServer = server;
				return true;
			} else {
				return false;
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void acceptSession() throws IOException {
		lock.lock();
		try {
			/*
			 *  TODO We are accepting a remote control request.  Push a local shell with a proxy context
			 *  on the shell stack and flag that initialization has completed.  Confirm acceptance of the 
			 *  remote control request by sending an ACK to the client.  The CLI of the newly installed shell
			 *  will be executed by the underlying CLI as part of the "accept" command.
			 */
			 LocalShell shell = LocalShell.createRemotelyControlled(SHELL_MANAGER.getCurrentShell().getLocal(), 
					ProxyContext.createProxyContext(this.getCurrentSession().getBasicRemote()));
             ShellManager.getShellManager().addShell(shell);
             currentServer.endInitialization();
             currentServer.getSession().getBasicRemote().sendText(ACK);
		} finally {
			lock.unlock();
		}
	}
	
	public void rejectSession() {
		lock.lock();
		try {
			// TODO reject remote control request by closing the session (provide a reason!)
            CloseReason closeReason = new CloseReason(CloseCodes.CANNOT_ACCEPT, "request rejected");
			currentServer.getSession().close(closeReason);
			currentServer = null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void closeCurrentSession() {
		lock.lock();
		try {
			// TODO normal shutdown of remote control session (provide a reason!)
			 CloseReason closeReason = new CloseReason(CloseCodes.NORMAL_CLOSURE,"Normal shutdown of the client session");
			 currentServer.getSession().close(closeReason);
			 currentServer = null;
			 } catch (IOException e) {
				e.printStackTrace();
			} finally {
			lock.unlock();
		}
	}

}
