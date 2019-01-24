# Aplazame Android SDK

## Dependency

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Then, add the library to your module `build.gradle`
```gradle
dependencies {
    compile 'com.github.aplazame:android-sdk:master-SNAPSHOT'
}
```

## How to use

First at all you need to configure AplazameSDK with your public token and the environment:

```java
AplazameSDK.setConfiguration(String token, boolean debug)
```

Then you can check if Aplazame is available for your order:

```java
AplazameSDK.checkAvailability(Double amount, String currency, AvailabilityCallback responseCallback)
```
The **AvailabilityCallback** contains the following 3 methods:
- `onAvailable`: Aplazame is available
- `onNotAvailable`: Aplazame is not available
- `onFailure`: Unknown error. It could be a timeout, Internet not available, so on.

Initialize the checkout in a WebView
```java
AplazameSDK.initializeAplazameWebView(Activity activity, WebView webView, JsWebViewEvents jsWebViewEvents);
```
**JsWebViewEvents** interface contains these 5 methods:
- `onPageStarted`: WebView event. Notify the host application that Aplazame page has started loading. 
- `onPageFinished`: WebView event. Notify the host application that Aplazame page has finished loading.
- `onReadyEvent`: Event received when the checkout is loaded in the WebView. Equivalent to the "checkout-ready" event.
- `onStatusChangeEvent (String status)`: Indicates a change of status of the checkout when it already has a result and updates the session while it remains open. Equivalent to the "status-change" event. Possible status values: "success", "pending" or "ko".
- `onCloseEvent (String status)`: Indicates that the checkout has been closed. Possible status values: "success", "pending", "dismiss" or "ko".

Add AplazameSDK in your onActivityResult

```java
AplazameSDK.onActivityResult(requestCode, resultCode, intent);
```

Add AplazameSDK in your onRequestPermissionsResult. There are 3 options:

- A toast with a default message if you don't accept the app permissions
```java
AplazameSDK.onActivityResult(requestCode, resultCode, intent);
```

- A toast with a custom message if you don't accept the app permissions
```java
AplazameSDK.onActivityResult(requestCode, resultCode, intent, String);
```

- A listener to customize your response. The methods are onAcceptPermissions() and onDeclinePermissions().
```java
AplazameSDK.onActivityResult(requestCode, resultCode, intent, OnRequestPermissionsListener);
```

More information about status event: https://aplazame.com/docs/api/checkout-parameters/checkout-postmessage/

## Usage example

Check the [demo project](https://github.com/aplazame/android-sdk/tree/master/demo) to see an example of their use.

Note: We will use a complete Checkout order. For more information: https://aplazame.com/docs/api/checkout-parameters/


1) Aplazame SDK configuration:

```java
AplazameSDK.setConfiguration("my public key", true)
```

2) Check Aplazame available

```java
AplazameSDK.checkAvailability(132.06, "EUR", new AvailabilityCallback() {
    @Override
    public void onAvailable() {
        // Enable checkout button for instance
    }

    @Override
    public void onNotAvailable() {
        // Hide the checkout button for instance
    }

    @Override
    public void onFailure() {
        // Hide the checkout button for instance (unknown error)
    }
});
```

3) Set Checkout ID

```java
AplazameSDK.setCheckout("checkout id")
```

4) Initialize WebView

```java
AplazameSDK.initializeAplazameWebView(webView, new JsWebViewEvents() {
    @Override
    public void onPageStarted() {}

    @Override
    public void onPageFinished() {}

    @Override
    public void onReadyEvent() {}

    @Override
    public void onStatusChangeEvent(String status) {
        switch (status) {
            case SUCCESS:
                break;
            case PENDING:
                break;
            case KO:
                break;
        }
    }

    @Override
    public void onCloseEvent(String status) {
        switch (status) {
            case SUCCESS:
                break;
            case PENDING:
                finish();
                break;
            case DISMISS:
                finish();
                break;
            case KO:
                break;
        }
    }
});
```

![alt text](https://raw.githubusercontent.com/aplazame/android-sdk/master/image2.png)

5) Add Aplazame SDK in onActivityResult

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    AplazameSDK.onActivityResult(requestCode, resultCode, intent);
}
```

6) Add Aplazame SDK in onRequestPermissionsResult

```java
@Override
protected void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    AplazameSDK.onRequestPermissionsResult(this, requestCode, grantResults);
}
```

License
-------

Aplazame is Copyright (c) 2018 Aplazame, inc. It is free software, and may be
redistributed under the terms specified in the [LICENSE](LICENSE) file.

About
-----
https://aplazame.com/
