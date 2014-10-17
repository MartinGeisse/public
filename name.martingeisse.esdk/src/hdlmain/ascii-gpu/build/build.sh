if [ -z "$1" ]; then
    echo "missing host IP address"
    exit
fi
ssh $1 "cd fpga; ./clear-fpga-files.sh"
scp -r ../* geisse@$1:./fpga/work
ssh $1 "source /opt/Xilinx/14.5/ISE_DS/settings64.sh; cd fpga/work/build; ./server-build.sh"
