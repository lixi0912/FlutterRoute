import 'package:flutter/material.dart';
import 'package:flutter_router/flutter_router.dart';

import 'demo.dart';

void main() {
  FlutterRouter.init(
      globalKey: new GlobalKey(debugLabel: "lixicode"),
      provider: (String path, String group) {
        if (!path.contains("demo")) {
          return null;
        }
        return (context, postcard) {
          return new Demo();
        };
      });

  FlutterRouter.registerRoute("/main/demo",
      (BuildContext context, PostCard postcard) {
    return new Demo();
  });

  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: new MainPage(),
    ));
  }
}

class MainPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Center(
      child: new Column(
        children: <Widget>[
          new RawMaterialButton(
              child: new Text(" to native page"),
              onPressed: () {
                FlutterRouter.pathOf("/main/demo2")
                  ..withValue("test", "fuck")
                  ..navigation(context);
              }),
          new RawMaterialButton(
              child: new Text(" to flutter second page"),
              onPressed: () {
                FlutterRouter.pathOf("/main/demo")
                  ..withValue("test", "fuck")
                  ..navigation(context);
              }),
          new RawMaterialButton(
              child: new Text(" page not found "),
              onPressed: () {
                FlutterRouter.pathOf("/main/fuck")
                  ..withValue("test", "fuck")
                  ..navigation(context, lost: (postcard, {result}) {
                    Scaffold.of(context).showSnackBar(
                        SnackBar(content: Text("page not found")));
                  });
              }),
        ],
      ),
    );
  }
}
