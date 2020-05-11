import numpy as np
import cv2
from PIL import Image
from matplotlib import pyplot as plt
import matplotlib.image as mpimg
import glob
import pytesseract
from imutils.perspective import four_point_transform
from imutils import contours
import imutils
pytesseract.pytesseract.tesseract_cmd = r"C:\\Program Files (x86)\\Tesseract-OCR\\tesseract.exe"
import socket
import os
import _thread
import math

def extract_text(image,no_of_ids):
    ids=[]
    image=cv2.resize(image,(850,850))
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    blurred = cv2.GaussianBlur(gray, (5,5), 0)
    edged = cv2.Canny(blurred, 30, 110, 30)
    # find contours in the edge map, then sort them by their
    # size in descending order
    cnts = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL,
        cv2.CHAIN_APPROX_SIMPLE)
    cnts = imutils.grab_contours(cnts)
    cnts = sorted(cnts, key=cv2.contourArea, reverse=True)
    
        # loop over the contours
    displayCnt = None
    i=0
    n_id=no_of_ids
    for c in cnts:        
        peri = cv2.arcLength(c, True)
        approx = cv2.approxPolyDP(c, 0.02 * peri, True)
        if len(approx) == 4:
            displayCnt = approx
            output = four_point_transform(image, displayCnt.reshape(4, 2))
            if(output.shape[1]>output.shape[0]):
                dim = (300, 180)
                output = cv2.resize(output, dim) 
                crop_img = output[115:145, 90:160]
            else :
                dim = (180, 300)
                output = cv2.resize(output, dim) 
                crop_img = output[195:215, 40:100]   
            ids.append(pytesseract.image_to_string(crop_img,lang='eng'))
            i=i+1  
            if i==n_id:
                break         
    with open('list.txt', 'w') as filehandle:
         filehandle.writelines("%s\n" % i for i in ids) 
def threaded(client,addr):
    #receive the number of ids + image size and calculate number of expected frames
    value=client.recv(1024)
    print(value)
    size=int(value[0:len(value)-2])
    id_num=int(value[len(value)-1:len(value)])
    loop=math.ceil(size/1024)
    print(size)
    print(id_num)
    print(loop)
    it=0
    #receive image from android 
    with open('input.jpeg', 'wb') as img:
        while (it<loop):
            it=it+1
            data = client.recv(1024)
            img.write(data)
            print(data)
        print ('image is recieved!')
    #read the image sent by the client 
    img1 = cv2.imread(r"F:\python\input.jpeg")
	#pass the image to extract_text function to get the id 
    extract_text(img1,id_num) 
    # Send output to client
    print("Beginning File Transfer")
    f = open("list.txt", 'rb')
    client.send(f.read(4096))
    f.close()
    print("Transfer Complete")
    #close socket
    client.close()

            
