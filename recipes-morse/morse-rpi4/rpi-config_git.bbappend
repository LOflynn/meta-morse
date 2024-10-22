do_deploy:append() {
	echo "dtoverlay=mm_wlan-overlay" >> $CONFIG
	echo "dtoverlay=morse-ps-overlay" >> $CONFIG
}

