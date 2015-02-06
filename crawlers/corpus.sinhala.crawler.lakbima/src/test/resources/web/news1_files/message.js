window.addEvent('domready', function() {
		
				

var tmjmosmsg,fx;
function pload(){
	 tmjmosmsg=$$('dl#system-message');
	 if($type(tmjmosmsg[0])=='element'){
	 var el=tmjmosmsg[0];
	 el.setStyle('overflow','hidden');
	 var h=el.getSize().size.y;
	 fx = new Fx.Styles(el, {duration:900, wait:false});
	 //scroll to message
	 winScroller = new Fx.Scroll(window);
	 winScroller.toElement($('header'));
	 //delayed start, then remove the html element upon completion
	(function(){
		fx.start({
			'margin-top':-1*h.toInt(),
			opacity:0
			}).chain(function(){el.remove();});
			}).delay(3500);
			}
		};
		 window.addEvent('load',function(){
			 pload();
		 }
);

});


