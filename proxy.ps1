netsh interface portproxy delete v4tov4 listenport="4000"
$remoteport = wsl ifconfig eth0 `| grep "inet "
$found = $remoteport -match '\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}';
if( $found ) { $remoteport = $matches[0]; } else {
    echo "wsl2 cannot be found";
    exit;
}
netsh interface portproxy add v4tov4 listenport="4000" connectaddress="$remoteport" connectport="8065"