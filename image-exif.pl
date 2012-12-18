use Image::EXIF;
use Geohash;
use Path::Class;

my $dir = dir('/PATH/TO/IMAGES/');
my $geo = Geohash->new();

while (my $file = $dir->next) {
	next if $file->is_dir();
	my $exif = Image::EXIF->new($file);
	my $exif_info = $exif->get_image_info();
	my $geo_locale = @$exif_info{'Latitude', 'Longitude'};
	my $filename = $file->basename;
	my $hash = $geo->encode(@$exif_info{'Latitude'}, @$exif_info{'Longitude'});
	print "$filename\t@$exif_info{'Longitude','Latitude'}\t\n";
}

#my $exif = Image::EXIF->new('/home/sbarnesisrael/.InfoSec/IMG/EXIF/smart-13.jpg');
#print "GPS Coords: @$all_info{'Longitude','Latitude'}\n";
