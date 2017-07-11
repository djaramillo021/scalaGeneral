

import com.djaramillo.rutinas.visual.BusquedaArchivos
object TestRegexAs extends App {

  
//  val lineaCls="Public Function recuperarDescripcionTipoDocumento(tipo As Integer) As String";
//  val sldCls=BusquedaArchivos.buscarTipoDatoLinea(lineaCls);
//  
//  println(sldCls)
//  
//  
//  
    val lineaFrm=" '   Attribute VB_Name = \"acrComprobantePagoLCC\"";
  val sldFrm=BusquedaArchivos.buscarAliasLinea(lineaFrm);
  
  println(sldFrm)
  
  
  val lineaComment=" '  Private pasdds Type tPosicion ";
  val sldCmt=BusquedaArchivos.buscarEstructuraLinea(lineaComment);
  
  println(sldCmt)
  

//  
//  
  /*
  val lineaOp=" Option Explicit ";
  val sldOp=BusquedaArchivos.buscarOptionExplicitLinea(lineaOp);
  
  println(sldOp)
  
  */
  

    /*
  val lineaMB="'cmdBuscar.Enabled = False ";
  val sldMB=BusquedaArchivos.buscarBaseDatosLinea(lineaMB);
  
  
  println(sldMB)


  val lineaTux="Bas/ImplementacionDeTransacciones.bas:   If Not mensaje.EjecutarServicioSinMensaje(DADADADA.ASDAS_SA) Then";
  val sldTux=BusquedaArchivos.buscarEjecutarServicio_MensajeLinea(lineaTux);
  
  println(sldTux)
  
  */
}