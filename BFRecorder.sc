BFRecorder { var <>recpath, <>recorder, <>hoaOrder, <>node, <>bus, <>numChans, <>vstfx;
	var win, btns, txt, list, layout;

	*new {|recpath, recorder, hoaOrder, node, bus, numChans, vstfx|
		^super.newCopyArgs(recpath, recorder, hoaOrder, node, bus, numChans, vstfx).initGUI;
	}

	initGUI {
	win = win = Window("Ambisonic Recorder", Rect(0.0, 543.0, 140, 107)).front.background_(Color.white);
	txt = 2.collect{|i| TextField().background_(Color.white)};
	btns = 2.collect{|i| Button()};
	list = ListView().items_(["ACN","N3D","SN3D"]).selectionMode_(\multi);
	btns[0].states_([["Load default HOA VST chain", Color.black, Color.white]]).action_({|i|if(i.value==0) {vstfx.editor}});
	btns[1].states_([["Record Arm", Color.red],["Record HOA", Color.red, Color.new255(255, 140, 0)], ["Stop", Color.black, Color.red]]).action_({|i|
		var recDur = Routine{
			{
				{ txt[1].string_(recorder.duration.asTimeString) }.defer;
				0.1.wait;
			}.loop;
		};
		case
		{i.value==0}{recorder.stopRecording; recDur.reset}
		{i.value==1}{recorder.prepareForRecord(recpath++"%_HOA%_%%%.wav".format(Date.getDate.stamp, hoaOrder, txt[0].string, list.items[list.selection.value]), numChans)}
		{i.value==2}{recorder.record( bus: bus,numChannels:  numChans, node: node);recDur.play;};
	});

	win.layout_(GridLayout.rows(
		[[btns[0], columns: 2]],
		[[list, columns: 2]],
		[[txt[0], columns: 2]],
		[[btns[1]] ,[txt[1]]],
	));
	}
}


