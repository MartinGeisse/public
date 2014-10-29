if [ -z "$1" ]; then
    echo "missing host IP address"
    exit
fi

# TODO remove
scp -r * geisse@$1:./fpga/work/build

ssh $1 "source /opt/Xilinx/14.5/ISE_DS/settings64.sh; cd fpga/work/build; ./server-program.sh"
