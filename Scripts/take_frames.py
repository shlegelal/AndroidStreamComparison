import os
import cv2

pathIn = "video/"
pathOut = "frames/"
listing = os.listdir(pathIn)
for vid in listing:
    folder = pathOut + vid[:-4]
    os.mkdir(folder)
    vid = pathIn + vid
    vidcap = cv2.VideoCapture(vid)
    count = 0
    vidcap.set(cv2.CAP_PROP_POS_FRAMES, count)
    success, image = vidcap.read()
    print(vidcap.get(cv2.CAP_PROP_FRAME_COUNT))
    while success:
        print('Read a new frame: ', success, ' ', count)
        cv2.imwrite(folder + "/frame%d.jpg" % count, image)  # save frame as JPEG file
        vidcap.set(cv2.CAP_PROP_POS_FRAMES, count)
        success, image = vidcap.read()
        count += 200
