# Garden manager

The garden manager software is made to control a garden with keywords. It then sends data to an arduino which is made to control pumps and lights. The arduino sends its captors data to the garden manager software which can display them on an human machine interface.

The keywords are retrieved from a Mosquitto topic available on the localhost called "speech".

This program was developped with Maven, so that it can easily be imported on your computer
