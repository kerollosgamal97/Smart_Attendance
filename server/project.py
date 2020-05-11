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