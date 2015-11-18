package com.kuaizhan.form.common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPUtil {

	final static Logger log = LoggerFactory.getLogger(IPUtil.class);

	public static String getHostLanIP() {
		List<InetAddress> addresses = new ArrayList<InetAddress>();

		try {
			Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
			while (ifs.hasMoreElements()) {
				NetworkInterface nic = ifs.nextElement();
				if (nic.isLoopback() || !nic.isUp()) {
					continue;
				}

				Enumeration<InetAddress> addrs = nic.getInetAddresses();
				while (addrs.hasMoreElements()) {
					InetAddress addr = addrs.nextElement();
					if (addr.isAnyLocalAddress() || addr.isLinkLocalAddress()
							|| addr.isLoopbackAddress() || addr.isMulticastAddress()) {
						continue;
					}

					addresses.add(addr);
				}
			}
		} catch (SocketException e) {
			log.error("Get network interface failed", e);
		}

		for (InetAddress addr : addresses) {
			if (isIPv4Addr(addr) && isLanAddr(addr)) {
				return addr.getHostAddress();
			}
		}
		return null;
	}

	/**
	 * Whether an InetAddress is IPv4.
	 * 
	 * @param addr
	 * @return
	 */
	public static boolean isIPv4Addr(InetAddress addr) {
		return addr != null && addr instanceof Inet4Address;
	}

	/**
	 * Whether an InetAddress is LAN address.
	 * 
	 * @param addr
	 * @return
	 */
	public static boolean isLanAddr(InetAddress addr) {
		if (addr == null) {
			return false;
		}

		String ip = addr.getHostAddress();
		if (ip == null) {
			return false;
		}

		int[] decs = new int[4];
		try {
			int i = 0;
			for (String decStr : ip.split("\\.")) {
				decs[i++] = Integer.parseInt(decStr);
			}
		} catch (Exception e) {
			return false;
		}

		if (decs[0] == 10) {
			return true;
		}

		if (decs[0] == 172) {
			return decs[1] >= 16 && decs[1] <= 31;
		}

		if (decs[0] == 192) {
			return decs[1] == 168;
		}

		return false;
	}

}
