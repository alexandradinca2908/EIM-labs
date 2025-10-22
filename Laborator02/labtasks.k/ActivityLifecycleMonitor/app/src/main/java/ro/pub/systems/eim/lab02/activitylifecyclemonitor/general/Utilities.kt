package ro.pub.systems.eim.lab02.activitylifecyclemonitor.general

import android.content.Context
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import ro.pub.systems.eim.lab02.activitylifecyclemonitor.entities.Credential
import java.io.IOException

object Utilities {
    @JvmStatic
    fun allowAccess(context: Context, username: String, password: String): Boolean {
        try {
            Log.i(Constants.TAG, "username: $username password: $password")
            val xmlPullParserFactory = XmlPullParserFactory.newInstance()
            val xmlPullParser = xmlPullParserFactory.newPullParser()
            val inputStream = context.assets.open(Constants.CREDENTIAL_FILE)
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            xmlPullParser.setInput(inputStream, null)
            val credentials = parseXml(xmlPullParser)
            for (credential in credentials!!) {
                Log.i(
                    Constants.TAG,
                    "credential.getUsername(): " + credential.username + " credential.getPassword(): " + credential.password
                )
                if (credential.username.equals(username) && credential.password == password) {
                    return true
                }
            }
        } catch (xmlPullParserException: XmlPullParserException) {
            xmlPullParserException.printStackTrace()
            if (Constants.DEBUG) {
                Log.e(Constants.TAG, "An exception has occurred: " + xmlPullParserException.message)
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            if (Constants.DEBUG) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.message)
            }
        }
        return false
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun parseXml(xmlPullParser: XmlPullParser): List<Credential>? {
        var credentialsList: MutableList<Credential>? = null
        var eventType = xmlPullParser.eventType
        var currentCredential: Credential? = null
        while (eventType != XmlPullParser.END_DOCUMENT) {
            var tag: String? = null
            when (eventType) {
                XmlPullParser.START_DOCUMENT -> credentialsList = ArrayList()
                XmlPullParser.START_TAG -> {
                    tag = xmlPullParser.name
                    if (Constants.CREDENTIAL_TAG.equals(tag, ignoreCase = true)) {
                        currentCredential = Credential()
                    } else if (currentCredential != null) {
                        if (Constants.USERNAME_TAG.equals(tag, ignoreCase = true)) {
                            currentCredential.username = xmlPullParser.nextText()
                        } else if (Constants.PASSWORD_TAG.equals(tag, ignoreCase = true)) {
                            currentCredential.password = xmlPullParser.nextText()
                        }
                    }
                }

                XmlPullParser.END_TAG -> {
                    tag = xmlPullParser.name
                    if (Constants.CREDENTIAL_TAG.equals(
                            tag,
                            ignoreCase = true
                        ) && currentCredential != null
                    ) {
                        credentialsList!!.add(currentCredential)
                    }
                }

                XmlPullParser.END_DOCUMENT -> return credentialsList
            }
            eventType = xmlPullParser.next()
        }
        return credentialsList
    }
}
