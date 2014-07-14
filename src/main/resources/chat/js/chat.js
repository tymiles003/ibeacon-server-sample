// WebSocketサーバに接続
var ws = new WebSocket('ws://54.92.14.144:8088/');

// エラー処理
ws.onerror = function(e) {
	$('#chat-area').empty().addClass('alert alert-error').append('<button type="button" class="close" data-dismiss="alert">×</button>', $('<i/>').addClass('icon-warning-sign'), 'サーバに接続できませんでした。');
};

// メッセージ受信イベントを処理
ws.onmessage = function(event) {
	// 受信したメッセージを復元
	var data = JSON.parse(event.data);
	var item = $('<li/>').append($('<div/>').append($('<i/>').addClass('icon-user'), $('<small/>').addClass('meta chat-time').append(data.time)));

	// pushされたメッセージを解釈し、要素を生成する
	item.addClass('alert alert-success').prepend('<button type="button" class="close" data-dismiss="alert">×</button>').children('div').children('i').after(event.data);
	$('#chat-history').prepend(item).hide().fadeIn(300);
};

// WebSocketサーバ接続イベント
ws.onopen = function() {
};

// 発言イベント
textbox.onkeydown = function(event) {
};

// ブラウザ終了イベント
window.onbeforeunload = function() {
};
