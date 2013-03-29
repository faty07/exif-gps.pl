my $myLat = '33 deg 31\' 1.00" N';
my $myLong = '82 deg 4\' 41.00" W';
my $degrees = substr($myLat,index($myLat,"deg"), -3);
#my $minutes = substr();
#my $seconds = substr();
my $latdirection = substr($myLat,-1);
my $longdirection = substr($myLong,-1);

my $calc_lat = 33 + (31/60) + (1/3600);
my $calc_lng = 82 + (4/60) + (41/3600);

print "Calc Lat: $calc_lat\n";
print "Calc Long: $calc_lng\n";
