
package com.x.business.zerodata.connection.helper;


/**
 * 
 
 *
 */
public class WifiState {
	/**
	* Wi-Fi is currently being disabled. The state will change to {@link #WIFI_STATE_DISABLED} if
	* it finishes successfully.
	*
	* @see #WIFI_STATE_CHANGED_ACTION
	* @see #getWifiState()
	*/
	public static final int WIFI_STATE_DISABLING = 0;
	/**
	 * Wi-Fi is disabled.
	 *
	 * @see #WIFI_STATE_CHANGED_ACTION
	 * @see #getWifiState()
	 */
	public static final int WIFI_STATE_DISABLED = 1;
	/**
	 * Wi-Fi is currently being enabled. The state will change to {@link #WIFI_STATE_ENABLED} if
	 * it finishes successfully.
	 *
	 * @see #WIFI_STATE_CHANGED_ACTION
	 * @see #getWifiState()
	 */
	public static final int WIFI_STATE_ENABLING = 2;
	/**
	 * Wi-Fi is enabled.
	 *
	 * @see #WIFI_STATE_CHANGED_ACTION
	 * @see #getWifiState()
	 */
	public static final int WIFI_STATE_ENABLED = 3;
	/**
	 * Wi-Fi is in an unknown state. This state will occur when an error happens while enabling
	 * or disabling.
	 *
	 * @see #WIFI_STATE_CHANGED_ACTION
	 * @see #getWifiState()
	 */
	public static final int WIFI_STATE_UNKNOWN = 4;

	/**
	 * Broadcast intent action indicating that Wi-Fi AP has been enabled, disabled,
	 * enabling, disabling, or failed.
	 *
	 * @hide
	 */
	public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";

	/**
	 * The lookup key for an int that indicates whether Wi-Fi AP is enabled,
	 * disabled, enabling, disabling, or failed.  Retrieve it with
	 * {@link android.content.Intent#getIntExtra(String,int)}.
	 *
	 * @see #WIFI_AP_STATE_DISABLED
	 * @see #WIFI_AP_STATE_DISABLING
	 * @see #WIFI_AP_STATE_ENABLED
	 * @see #WIFI_AP_STATE_ENABLING
	 * @see #WIFI_AP_STATE_FAILED
	 *
	 * @hide
	 */
	public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
	/**
	 * The previous Wi-Fi state.
	 *
	 * @see #EXTRA_WIFI_AP_STATE
	 *
	 * @hide
	 */
	public static final String EXTRA_PREVIOUS_WIFI_AP_STATE = "previous_wifi_state";
	/**
	 * Wi-Fi AP is currently being disabled. The state will change to
	 * {@link #WIFI_AP_STATE_DISABLED} if it finishes successfully.
	 *
	 * @see #WIFI_AP_STATE_CHANGED_ACTION
	 * @see #getWifiApState()
	 *
	 * @hide
	 */
	public static final int WIFI_AP_STATE_DISABLING = 10;
	/**
	 * Wi-Fi AP is disabled.
	 *
	 * @see #WIFI_AP_STATE_CHANGED_ACTION
	 * @see #getWifiState()
	 *
	 * @hide
	 */
	public static final int WIFI_AP_STATE_DISABLED = 11;
	/**
	 * Wi-Fi AP is currently being enabled. The state will change to
	 * {@link #WIFI_AP_STATE_ENABLED} if it finishes successfully.
	 *
	 * @see #WIFI_AP_STATE_CHANGED_ACTION
	 * @see #getWifiApState()
	 *
	 * @hide
	 */
	public static final int WIFI_AP_STATE_ENABLING = 12;
	/**
	 * Wi-Fi AP is enabled.
	 *
	 * @see #WIFI_AP_STATE_CHANGED_ACTION
	 * @see #getWifiApState()
	 *
	 * @hide
	 */
	public static final int WIFI_AP_STATE_ENABLED = 13;
	/**
	 * Wi-Fi AP is in a failed state. This state will occur when an error occurs during
	 * enabling or disabling
	 *
	 * @see #WIFI_AP_STATE_CHANGED_ACTION
	 * @see #getWifiApState()
	 *
	 * @hide
	 */
	public static final int WIFI_AP_STATE_FAILED = 14;

}
