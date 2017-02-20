# GroupMessenger
This project builds on the Simple Messenger. Group Messenger 1 is an Android app that can send (multicast) a message to multiple AVDs (devices) and store them in a permanent key-value storage.

Some of the app features are:

Multicast user-entered message to app instances on all AVDs (devices), including the sender.

Send/Receive messages multiple times.

Handle concurrent messages.

The app assigns a sequence number (starting from 0 and incrementing by 1 for each message) to each message it receives. Each message and its sequence number are stored as a pair in a SQLite database (key: sequence number, value: message). If a new value is inserted using an existing key, then only the most recent value is stored. The history of values under the same key is not preserved.


Developed By
------------
* Arbaaz Singh Sidhu - <arbaazsi@buffalo.edu>

License
-------

    Copyright 2016 Arbaaz Singh Sidhu

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


