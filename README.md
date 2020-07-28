![logo](https://i.imgur.com/8CxiWL5.jpg)

# METAR Utility
METAR Utility is an Android Application that reports Meteorological Aerodrome Reports from an API and displays it to a user via a graphical user interface. This application is intended to help pilots decipher complicated METAR reports into a human readable format, and also provide relevant airport information.

This application queries the user for a 4-letter ICAO code, then performs a GET request to the AVWX API to retrieve real-time METAR information and airport information to display it to the user. The data is from the open source AVWX REST API for Aviation Weather.

The application provides the following data to aviators:

**METAR:**
- Time of Report
- Flight Rules
- Wind conditions
- Visibility Information
- Runway Visual Range Information
- Current Weather Conditions
- Current Cloud Conditions
- Temperature and Dewpoint
- Altimeter Information

**Airport Information:**
- Integrated Google Map to show airport
- Coordinates of Airport
- Geographic Information
- Runway Information
    - Runway Identifiers
    - Runway Length
    - Runway Width
    - Surface Type
    - Lighting Information
    
The application has been [published onto the Google Play Store](https://play.google.com/store/apps/details?id=com.metarutility.metarutility).

**Screenshots:**

![alt text](https://i.imgur.com/6PlRQJr.jpg)

API Source: https://avwx.rest/
