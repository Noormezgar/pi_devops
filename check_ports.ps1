$ports = 8081,8082,8084,8090,8091,8092,8093,8761
foreach ($p in $ports) {
    $r = Test-NetConnection -ComputerName localhost -Port $p -WarningAction SilentlyContinue
    "$p $($r.TcpTestSucceeded)"
}
