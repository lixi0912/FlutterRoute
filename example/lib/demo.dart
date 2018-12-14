import 'package:flutter/material.dart';
import 'package:flutter_router/flutter_router.dart';

class Demo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(" scond flutter page "),
      ),
      body: new DemoPage(),
    );
  }
}

class DemoPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Center(
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
