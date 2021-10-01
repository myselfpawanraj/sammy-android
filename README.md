## Sammy
#### *Accessibility Media Player*

Sammy is a smart media player, that enables understanding of videos for the visually-disabled and quicker navigation for everyone

- ✨ Sammy is a multimedia player that can help you find, in a video, anything from a simple phrase that was spoken to a scene that you can describe in words.
- ✨ We combined OCR (Optical Character Recognition), ASR (Automatic Speech Recognition) and Scene Understanding (SDD) to describe the scene taking place in the video and to make this information searchable.

### WHAT ARE WE SOLVING?
Imagine a world where even specially-enabled people are able to understand and navigate through video content and where nobody has to spend ages searching for a single line in hour-long videos.
Today, we don’t have a system that can make the content inside videos searchable and describable. Videos/movies for the optically-disabled have to be professionally voiced through an expensive process which makes them inaccessible. This lack of a system is also a problem when one is trying to pick up a small piece of information in a long video.

### Features

- Can search for a particular phrase through your voice
- It can Describe the video with voice and text
- User can upload video of their choice

### Tech Used
Sammy uses a number of open source projects to work properly:
- #### Android
    - Java
    - XML
    - ExoPlayer
    - Retrofit - for REST API
    - Text to Speak by Google
    - Speech-to-Text by Google
    - MVP architecture

- #### Backend
    - NodeJS
    - Web/Worker processes
        - Packages that internally use Redis - for job queues
    - Video API
        - Packages that internally use ffmpeg - Detect scenes, extract frames
        - Azure Cloud Vision
        - Tesseract - for OCR
        - Sharp
        - Express
    - Audio API
        - Packages that internally use ffmpeg - to extract audio
        - Azure cognitive speech services/DeepSpeech

![](https://plytonrexus.github.io/sammy-web/images/sm-graph.svg)

### Screenshots
<div class="row">
      <img src="https://raw.githubusercontent.com/myselfpawanraj/sammy-android/master/Screenshots/home.png" width="30%" title="Sammy home" alt="">
        &emsp;
      <img src="https://raw.githubusercontent.com/myselfpawanraj/sammy-android/master/Screenshots/about_us.png" width="30%" title="About us" alt="">
        &emsp;
      <img src="https://raw.githubusercontent.com/myselfpawanraj/sammy-android/master/Screenshots/select_media.png" width="30%" title="Select media" alt="">
</div>
<br>
<div class="row">
      <img src="https://raw.githubusercontent.com/myselfpawanraj/sammy-android/master/Screenshots/loading.png" width="30%" title="Sammy home" alt="">
        &emsp;
      <img src="https://raw.githubusercontent.com/myselfpawanraj/sammy-android/master/Screenshots/scene_describer.png" width="30%" title="About us" alt="">
        &emsp;
      <img src="https://raw.githubusercontent.com/myselfpawanraj/sammy-android/master/Screenshots/search.png" width="30%" title="Select media" alt="">
</div>
<br>

### Related links
- ###### [GitHub Repository | Web](https://github.com/PlytonRexus/sammy-node)
- ###### [GitHub Repository | Video Node](https://github.com/PlytonRexus/sammy-node)
- ###### [GitHub Repository | Audio Node](https://github.com/PlytonRexus/sammy-node-audio)
- ###### [Sammy - Slides/Presentatiom](https://docs.google.com/presentation/d/1FNzMfHjVQVPnZ-rpnFb149bxvdE5WuJT0RT1AOqp2r0/edit?usp=sharing)

***
##### You can run this Android app on [Appetize.io](https://appetize.io/app/byzq870wtf7b92qna9wt5hmubc?device=pixel4&scale=75&orientation=portrait&osVersion=10.0&deviceColor=black)
