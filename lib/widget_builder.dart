import 'package:flutter/material.dart';
import 'flutter_router.dart';

typedef Widget WidgetBuilderCompat(BuildContext context, PostCard card);
typedef WidgetBuilderCompat WidgetBuilderProvider(String path, String group);

class WidgetBuilderFactory {
  final Map<String, WidgetBuilderCompat> _cache = {};
  WidgetBuilderProvider _provider;

  void registerProvider(WidgetBuilderProvider provider) {
    this._provider = provider;
  }

  void registerRoute(String key, WidgetBuilderCompat builder) {
    _cache.update(key, (value) {
      return builder;
    }, ifAbsent: () {
      return builder;
    });
  }

  WidgetBuilderCompat findBuilder(String path, {String group}) {
    WidgetBuilderCompat builder = _cache[path];
    if (null == builder) {
      builder = _provider(path, group);
    }
    return builder;
  }
}
