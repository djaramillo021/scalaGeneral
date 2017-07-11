

import com.djaramillo.rutinas.visual.ElementosVisual
object TestRegex extends App {

  
  val lineaCls="Class=cBeneficiarioAdicional; Falabella\\Clases Deposito\\cBeneficiarioAdicional.cls";
  val sldCls=ElementosVisual.obtenerElemento(lineaCls);
  
  println(sldCls)
  
  
  
    val lineaFrm="Form=Falabella\\LCCPagoAnticipadoDeCuotas.frm";
  val sldFrm=ElementosVisual.obtenerElemento(lineaFrm);
  
  println(sldFrm)
  
  
  
}