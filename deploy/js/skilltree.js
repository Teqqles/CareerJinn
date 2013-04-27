var bubble = {};

bubble.skill = 0;

function drawBubble( label, positionx, positiony, relevence, trunk_location ) {
    var can = document.getElementById('canvas1');
    var context = can.getContext('2d');

    var branch_offset = relevence / 8;
    var random_offset = Math.random() * branch_offset;

    context.fillStyle = '#A27020';
    context.beginPath();
    context.moveTo( positionx, positiony);
    context.lineTo( trunk_location, positiony + branch_offset + random_offset );
    context.lineTo( trunk_location, positiony + branch_offset * 3 + random_offset );
    context.lineTo( positionx, positiony );
    context.closePath();
    context.fill();

    var font_size = relevence / 3;
    context.font = "bold " + font_size + "px sans-serif";
    context.textBaseline = "top";
    var metrics = context.measureText( label );
    var width = metrics.width;

    context.globalAlpha= relevence / 100;
    context.fillStyle = "#70b658";
    context.beginPath();
    var radius = ( width / 2 ) + relevence / 10; // for example
    context.arc( positionx, positiony, radius, 0, Math.PI * 2);
    context.closePath();
    context.fill();
    context.globalAlpha=1;
    context.fillStyle = "#580d03"; // font color to write the text with
    context.fillText( label, positionx-width/2 ,positiony-font_size/1.5 );
}

function drawTrunk( x, y ) {
    var c2 = document.getElementById('canvas1').getContext('2d');
    c2.fillStyle = '#A27020';
    c2.beginPath();
    c2.moveTo( x, 0);
    c2.lineTo( x-25, y);
    c2.lineTo( x+25, y);
    c2.lineTo( x, 0);
    c2.closePath();
    c2.fill();
}

function addSkill( skillName, count ) {
    if( count > 100 ) {
        count = 100;
    }
    if( count > 0 ) {
        count = count/100*40;
    }
    count += 40;
    if ( bubble.skill++ > 5 ) {
        return; // we only display the top skills
    }
    var x = (bubble.skill+1) % 2? 150 : 350; //display the skill on the left or right of the trunk
    var y = 450 - ( bubble.skill * 65 );
    drawBubble( skillName, x, y, count, 250 );

}

function drawCanvas() {
    drawTrunk( 250, 500 );
}
