rm -rf xst
mkdir xst
mkdir xst/tmp
xst -ifn xst-build-script.xst -ofn xst.log -intstyle xflow
ngdbuild -intstyle xflow -dd _ngo -nt timestamp -uc ../src/toplevel/toplevel.ucf -p xc3s500e-fg320-4 toplevel.ngc toplevel.ngd
map -w -intstyle xflow -p xc3s500e-fg320-4 -cm area -ir off -pr off -o toplevel_map.ncd toplevel.ngd toplevel.pcf
par -w -intstyle xflow -ol high -pl high -rl high toplevel_map.ncd toplevel_par.ncd toplevel.pcf
bitgen -intstyle xflow -f bitgen-script.ut toplevel_par.ncd toplevel.bit
