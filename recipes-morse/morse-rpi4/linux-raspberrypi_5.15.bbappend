
inherit kernel-devicetree
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://morse_patches.patch \
		file://hostap_crypto.cfg \
		file://mm_wlan-overlay.dts \
		file://morse-ps-overlay.dts \
		"

KERNEL_DEVICETREE:append = " overlays/mm_wlan-overlay.dtbo overlays/morse-ps-overlay.dtbo"

do_compile:prepend() {
    cp ${WORKDIR}/mm_wlan-overlay.dts ${S}/arch/arm/boot/dts/overlays/
    cp ${WORKDIR}/morse-ps-overlay.dts ${S}/arch/arm/boot/dts/overlays/
}

do_install:append() {
    install -d ${D}/boot/overlays/
    install -m 0644 ${B}/arch/arm/boot/dts/overlays/mm_wlan-overlay.dtbo ${D}/boot/overlays/
    install -m 0644 ${B}/arch/arm/boot/dts/overlays/morse-ps-overlay.dtbo ${D}/boot/overlays/
}

PACKAGES += "${PN}-overlays"
FILES:${PN}-overlays = "/boot/overlays/*.dtbo"
