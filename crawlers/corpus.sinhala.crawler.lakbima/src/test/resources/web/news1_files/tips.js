window.addEvent('domready', function() {
		var Tips0 = new Tips($$('#gotop'), {
			initialize:function(){
				this.fx = new Fx.Style(this.toolTip, 'opacity', {duration: 500, wait: false}).set(0);
			},
			onShow: function(toolTip) {
				this.fx.start(1);
			},
			onHide: function(toolTip) {
				this.fx.start(0);
			}
		});
		var Tips1 = new Tips($$('#tip-fade'), {
			initialize:function(){
				this.fx = new Fx.Style(this.toolTip, 'opacity', {duration: 500, wait: false}).set(0);
			},
			onShow: function(toolTip) {
				this.fx.start(1);
			},
			onHide: function(toolTip) {
				this.fx.start(0);
			}
		});
		var Tips2 = new Tips($$('#tip-white'), {
			className: 'white',
			showDelay: 10,
			hideDelay: 100
		});
		var Tips3 = new Tips($$('#tip-gray'), {
			className: 'gray',
			showDelay: 10,
			hideDelay: 100
		});
		var Tips4 = new Tips($$('#tip-blue'), {
			className: 'blue',
			showDelay: 10,
			hideDelay: 100
		});
		var Tips4 = new Tips($$('#tip-green'), {
			className: 'green',
			showDelay: 10,
			hideDelay: 100
		});
		var Tips4 = new Tips($$('#tip-red'), {
			className: 'red',
			showDelay: 10,
			hideDelay: 100
		});
});


