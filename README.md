# Vert.x + WebSocket + Redis

リアルタイムチャットを作成するトレーニング

# 手順

## 新しいterminalを開きます

vertx runmod jp.ad.iij.nakam~vertx-websocket~0.0.1 -conf conf.json

## 動作確認1

ブラウザからlocalhostを開く

## 動作確認2

ブラウザからfile:///{PROJECT_ROOT}/client/index.htmlを開く

# redisの中身を確認

centos@localhost vertx-websocket $ redis-cli --raw

127.0.0.1:6379> subscribe chat:message

# クラスタ構成の方法
vertx run{|mod} で起動するときに "-cp cluster.xml"を追加する

