window.addEvent('domready', function() {
		
				

	 	var list = $$('.module_menu ul.menu li a');
		list.each(function(element) {
		 
			var fx = new Fx.Styles(element, {duration:400, wait:false, transition: Fx.Transitions.Expo.easeOut});
		 
			element.addEvent('mouseenter', function(){
				fx.start({
					'margin-left': -15
				});
			});
		 
			element.addEvent('mouseleave', function(){
				fx.start({
					'margin-left': 0
				});
			});
		 
		});

	 	var list = $$('ul#mainlevel-nav li a');
		list.each(function(element) {
		 
			var fx = new Fx.Styles(element, {duration:200, wait:false, transition: Fx.Transitions.Expo.easeOut});
		 
			element.addEvent('mouseenter', function(){
				fx.start({
					'padding-top': 3			        
				});
			});
		 
			element.addEvent('mouseleave', function(){
				fx.start({
				    'padding-top': 0			        
				});
			});
		 
		});


							var list = $$('a.logo, a.contentpagetitle');
		list.each(function(element) {
		 
			var fx = new Fx.Styles(element, {duration:250, wait:false, transition: Fx.Transitions.linear});
		 
			element.addEvent('mouseenter', function(){
				fx.start({							        
			         'opacity': '0.6'			
				});
			});
		 
			element.addEvent('mouseleave', function(){
				fx.start({				    		        
			         'opacity': '1'						
				});
			});
		 
		});


});


