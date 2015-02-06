window.addEvent('domready', function() {

		var scroll = new Fx.Scroll(Window, {
		    wait: false,
		    duration: 800,
		    offset: {'x': 0, 'y': 0},
		    transition: Fx.Transitions.Quad.easeInOut
		});
		 
		$('gotop').addEvent('click', function(event) {
		    event = new Event(event).stop();
		    scroll.toElement('wrapper');
		}); 

});