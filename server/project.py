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