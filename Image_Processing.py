import os
import cv2

main_path = "C:\\Users\\Aram.Azatyan\\Desktop\\HW1\\ImageProcessing_HW1\\CSHandwriting"
subs = os.listdir(main_path)

for sub in subs:
    cur_path = os.path.join(main_path, sub)
    image = os.listdir(cur_path)[0]
    img_abs_path = os.path.join(cur_path, image)

    img = cv2.imread(img_abs_path)

    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    blur = cv2.GaussianBlur(gray, (0,0), sigmaX=33, sigmaY=33)
    divide = cv2.divide(gray, blur, scale=255)
    thresh = cv2.threshold(divide, 0, 255, cv2.THRESH_BINARY+cv2.THRESH_OTSU)[1]
    kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (3,3))
    morph = cv2.morphologyEx(thresh, cv2.MORPH_CLOSE, kernel)

    cv2.imwrite(os.path.join(cur_path, "binary.png"), thresh)

cv2.waitKey(0)
cv2.destroyAllWindows()
