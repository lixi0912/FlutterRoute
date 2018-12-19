import 'dart:async';
import 'dart:math';
import 'package:flutter/services.dart';
import 'package:flutter/material.dart';
import 'package:meta/meta.dart';
import 'widget_builder.dart';
import 'constants.dart';

class FlutterRouter {
  FlutterRouter._internal();

  static Future<String> get platformVersion async {
    final String version =
        await _Router._channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static void init({GlobalKey globalKey, WidgetBuilderProvider provider}) {
    _Router._singleton._globalKey = globalKey;
    _Router._singleton._factory.registerProvider(provider);
  }

  static void registerRoute(String key, WidgetBuilderCompat builder) {
    _Router._singleton._factory.registerRoute(key, builder);
  }

  static PostCard pathOf(String path, {String group}) {
    return _Router._singleton.build(path: path, group: group);
  }

  static PostCard uriOf(Uri uri) {
    return _Router._singleton.build(uri: uri);
  }

  @protected
  static void navigation(BuildContext context, PostCard postcard,
      {NavigationArrivalCallback arrival,
      NavigationFoundedCallback onFounded,
      NavigationLostCallback lost,
      NavigationInterruptCallback interrupt}) {
    _Router._singleton.navigation(context, postcard,
        onArrival: arrival,
        onFounded: onFounded,
        onLost: lost,
        interrupt: interrupt);
  }
}

// native call flutter -> flutter call native -> handled
//                                            -> flutter call self
class _Router {
  static const MethodChannel _channel = const MethodChannel('flutter_router');
  static final _Router _singleton = new _Router._internal();

  final WidgetBuilderFactory _factory = new WidgetBuilderFactory();
  GlobalKey _globalKey;

  _Router._internal() {
    _channel.setMethodCallHandler((MethodCall call) async {
      // call from native
      String method = call.method;
      if (method == Constants.ROUTER_CHANNEL_NAVIGATION_OF) {
        PostCard postcard = PostCard.of(call.arguments);
        return _navigation(_globalKey.currentContext, postcard,
            onLost: (postcard, {dynamic result}) {
          _channel.invokeMethod(
              Constants.ROUTER_CHANNEL_FOUNDED, call.arguments);
        });
      } else if (method == Constants.ROUTER_CHANNEL_NAVIGATION_ON_RESULT) {
        handleOnResult(_globalKey.currentContext, PostCard.of(call.arguments));
      }
    });
  }

  Future handleOnResult(BuildContext context, PostCard postcard) async {
    try {
      dynamic result = await _channel.invokeMethod(
          Constants.ROUTER_CHANNEL_NAVIGATION_ON_RESULT, postcard._arguments);
    } on MissingPluginException {
      Route route = Navigator.of(context).history.last;
      route.didComplete(postcard._arguments);
    }
  }

  Future navigation(BuildContext context, PostCard postcard,
      {NavigationArrivalCallback onArrival,
      NavigationFoundedCallback onFounded,
      NavigationLostCallback onLost,
      NavigationInterruptCallback interrupt}) async {
    try {
      dynamic result = await _channel.invokeMethod(
          Constants.ROUTER_CHANNEL_NAVIGATION_OF, postcard._arguments);

      if (result is String) {}
    } on MissingPluginException {
      return _navigation(context, postcard);
    }
  }

  Future<dynamic> _navigation(BuildContext context, PostCard postcard,
      {NavigationArrivalCallback onArrival,
      NavigationFoundedCallback onFounded,
      NavigationLostCallback onLost,
      NavigationInterruptCallback interrupt}) {
    Future<dynamic> future = new Future(() async {
      Route<dynamic> route = postcard.toRoute(context);
      if (null == route) {
        if (null != onLost) {
          onLost(postcard);
        }
        throw MissingPluginException(
            'No route for postcard[${postcard._path}]');
      }
      if (null != onFounded) {
        onFounded(postcard);
      }
      Future<dynamic> result = Navigator.of(context).push(route);
      if (null != onArrival) {
        onArrival(postcard);
      }
      return result;
    });
    return future;
  }

  PostCard build({String path, String group, Uri uri}) {
    if (null != uri) {
      path = uri.path;
      group = extractGroup(path);
    } else {
      group ??= extractGroup(path);
    }
    return PostCard(path: path, group: group, uri: uri);
  }

  String extractGroup(String path) {
    ArgumentError.checkNotNull(path, "path group");
    if (!path.startsWith('/')) {
      return null;
    }
    int index = path.indexOf('/', 1);
    if (index == -1) {
      return path.substring(1);
    }
    return path.substring(1, index);
  }
}

typedef NavigationArrivalCallback = void Function(PostCard postcard,
    {dynamic result});
typedef NavigationLostCallback = void Function(PostCard postcard,
    {dynamic result});
typedef NavigationFoundedCallback = void Function(PostCard postcard,
    {dynamic result});
typedef NavigationInterruptCallback = void Function(PostCard postcard,
    {dynamic result});

abstract class PostCard {
  factory PostCard({String path, String group, Uri uri, Map bundle}) =
      _PostCard;

  factory PostCard.of(Map args) = _PostCard._of;

  Map get _arguments;

  String get _path;

  String get _group;

  void withValue(String key, dynamic value);

  void navigation(
    BuildContext context, {
    NavigationArrivalCallback arrival,
    NavigationFoundedCallback founded,
    NavigationLostCallback lost,
    NavigationInterruptCallback interrupt,
  });

  Route<dynamic> toRoute(BuildContext context);
}

class _PostCard implements PostCard {
  final Uri _uri;
  final Map _arguments;

  factory _PostCard._of(Map args) {
    Uri uri;
    String uriStr = args['uri'];
    if (null != uriStr && uriStr.isNotEmpty) {
      uri = Uri.parse(uriStr);
    } else {
      uri = null;
    }
    return _PostCard._internal(args, uri);
  }

  _PostCard._internal(this._arguments, this._uri);

  factory _PostCard(
      {String path, String group, Uri uri, Map bundle = const {}}) {
    Map map = Map.from(bundle);
    map.putIfAbsent('path', () => path);
    map.putIfAbsent('group', () => group);
    map.putIfAbsent('uri', () => uri?.toString());
    return new _PostCard._internal(map, uri);
  }

  @override
  String get _path => _arguments['path'];

  @override
  String get _group => _arguments['group'];

  @override
  void withValue(String key, value) {
    _arguments.update(key, (old) {
      return value;
    }, ifAbsent: () {
      return value;
    });
  }

  @override
  void navigation(BuildContext context,
      {NavigationArrivalCallback arrival,
      NavigationFoundedCallback founded,
      NavigationLostCallback lost,
      NavigationInterruptCallback interrupt}) {
    FlutterRouter.navigation(context, this,
        arrival: arrival, onFounded: founded, lost: lost, interrupt: interrupt);
  }

  @override
  Route<dynamic> toRoute(BuildContext context) {
    WidgetBuilderCompat buildWidget =
        _Router._singleton._factory.findBuilder(_path, group: _group);
    if (null == buildWidget) {
      return null;
    }
    return new MaterialPageRoute(
        settings: new RouteSettings(name: _path),
        builder: (context) {
          return buildWidget(context, this);
        });
  }
}
