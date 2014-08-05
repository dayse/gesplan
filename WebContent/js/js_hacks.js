function maiorQue(a, b) {
        return a > b;
    }
function maiorQueIgual(a, b) {
        return a >= b;
    }
function menorQue(a, b) {
        return a < b;
    }
function menorQueIgual(a, b) {
        return a <= b;
    }
function ifAndAnd(a, b) {
        return a && b;
    }

function isIE()
{
  return /msie/i.test(navigator.userAgent) && !/opera/i.test(navigator.userAgent);
}

function ForGrafico3DSetColums(numMax,data){
    for (var i=0; i < numMax; i++)
    {
      data.addColumn('number', 'col'+i);
    }
}

function ForGrafico3DSetData(numRows,numCols,data,systemData,tooltipStrings,idx){

//alert(data);
	for (var i = 0; i < numRows; i++) 
	{
	  for (var j = 0; j < numCols; j++)
	  {
	   // var value = (Math.cos(i *  Math.PI / 180.0) * Math.cos(j * Math.PI / 180.0));
	    var value = systemData[i][j];
	    data.setValue(i, j, value);		
	    tooltipStrings[idx] = "x:" + i + ", y:" + j + " = " + value;
	    idx++;
	  }
	}
}