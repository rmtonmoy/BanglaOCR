// Build and run:
// g++ -o segmentlines segmentlines.cpp -llept -ltesseract
// Call: ./segmentlines FILE_NAME PATH_TO_OUTPUT

#include <tesseract/baseapi.h>
#include <leptonica/allheaders.h>
#include <string>
#include <sys/types.h>
#include <sys/stat.h>

// Write pix
// pixWrite("written.jpg", image, IFF_JFIF_JPEG);
// 3rd param: IFF_PNG

int main(int argc, char * argv[])
{
    Pix *image = pixRead(argv[1]);
    tesseract::TessBaseAPI *api = new tesseract::TessBaseAPI();
    api->SetPageSegMode(tesseract::PSM_AUTO);
    api->SetImage(image);

    // First parameter is the type of object.
    // Options: RIL_BLOCK, RIL_PARA, RIL_TEXTLINE, RIL_WORD, RIL_SYMBOL
    // Second parameter is if we want the object only to contain text.
    // true for text only, false for everything.
    Boxa* boxes = api->GetComponentImages(tesseract::RIL_TEXTLINE, true, NULL, NULL);

    // Create directory for lines
    std::string base_dir(argv[2]);
    base_dir += "/lines";
    mkdir(base_dir.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);

    printf("Found %d lines.\n", boxes->n);
    for (int i = 0; i < boxes->n; i++) {
        BOX* box = boxaGetBox(boxes, i, L_CLONE);
        fprintf(stdout, "line-[%d]: x=%d, y=%d, w=%d, h=%d\n",
                  i, box->x, box->y, box->w, box->h);

        // Save the images in the box region
        char name[100];
        sprintf(name, "%s/line-%d.png", base_dir.c_str(), i);
        PIX* croppedImage = pixClipRectangle(image, box, NULL);
        pixWrite(name, croppedImage, IFF_PNG);

        // Destroy box and pix
        boxDestroy(&box);
        pixDestroy(&croppedImage);
    }

    // Destroy used object and release memory
    api->End();
    pixDestroy(&image);

    return 0;
}
