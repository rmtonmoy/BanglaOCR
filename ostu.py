
# Ostu's Algorithm For Binarization
# Call python grayScale.py File_Name

import os
import sys
import time
import cv2
import numpy

img = cv2.imread(sys.argv[1], cv2.IMREAD_GRAYSCALE)
#median = cv2.medianBlur(img, 3)
#gauss = cv2.GaussianBlur(img,(5,5),0)

#ret1,th1 = cv2.threshold(median,0,255,cv2.THRESH_BINARY+cv2.THRESH_OTSU)
#ret2,th2 = cv2.threshold(gauss,0,255,cv2.THRESH_BINARY+cv2.THRESH_OTSU)
ret3,th3 = cv2.threshold(img,0,255,cv2.THRESH_BINARY+cv2.THRESH_OTSU)


#cv2.imwrite("median.jpg", th1)
#cv2.imwrite("gauss.jpg", th2)
cv2.imwrite("original.jpg", th3)



