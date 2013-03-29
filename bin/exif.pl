use Image::ExifTool;
use Data::Dumper;

$file_name = '/hom/sbarnesisrael/Downloads/res/pic9.jpg';
$exiftool = new Image::ExifTool;
$imageInfo = $exiftool->ImageInfo($file_name, 'Longitude', 'Latitude');

$success = $exiftool->GetInfo($file_name, \%options);
@tagList = $exiftool->GetFoundTags('File');
$info = $exiftool->GetInfo('ImageWidth', 'ImageHeight');

print Dumper(\@tagList);
print Dumper($info);
print Dumper($imageInfo);
