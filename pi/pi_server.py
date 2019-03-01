import time
import cv2
import struct
import socket
import runConsumerOWP
from flask import Flask,jsonify,request

cv2.waitKey(5000)
vc = cv2.VideoCapture(0)

cv2.waitKey(5000)

@app.route('/receive',methods=['GET'])
def buyService():
    service = request.data
    return request.data

def sendImageThroughSocket(socket,image):
    img_str = cv2.imencode('.jpg', image)[1].tostring()
    imageSize = len(img_str)
    val = struct.pack('!i', imageSize)
    socket.send(val)
    socket.send(img_str)
    print imageSize

def response(socket):
    status = s.recv(4)
    return status

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#s.connect(('10.12.5.208', 5555))
#s.connect(('192.168.0.102', 5555))
s.connect(('192.168.43.111', 8000))

while True:
    for x in range(2):
        ret, frame = vc.read()
    frame = cv2.flip(frame,0)
    sendImageThroughSocket(s,frame)
    print response(s)
    if request.data == "ok":
        runComsumerOWP.run()