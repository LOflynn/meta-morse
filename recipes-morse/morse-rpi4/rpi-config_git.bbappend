DESCRIPTION = "Append rpi-config to deploy device tree binary overlays to /boot/config.txt"

do_deploy:append() {
	echo "dtoverlay=mm_wlan-overlay" >> $CONFIG
	echo "dtoverlay=morse-ps-overlay" >> $CONFIG
}

