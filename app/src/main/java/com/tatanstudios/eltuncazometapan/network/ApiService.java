package com.tatanstudios.eltuncazometapan.network;


import com.tatanstudios.eltuncazometapan.modelos.carrito.ModeloCarrito;
import com.tatanstudios.eltuncazometapan.modelos.carrito.ModeloCarritoProductoEditar;
import com.tatanstudios.eltuncazometapan.modelos.carrito.ModeloCarritoTemporal;
import com.tatanstudios.eltuncazometapan.modelos.direccion.ModeloDireccion;
import com.tatanstudios.eltuncazometapan.modelos.eventos.ModeloEvento;
import com.tatanstudios.eltuncazometapan.modelos.historial.ModeloHistorial;
import com.tatanstudios.eltuncazometapan.modelos.informacion.ModeloInformacion;
import com.tatanstudios.eltuncazometapan.modelos.menu.ModeloMenuVertical;
import com.tatanstudios.eltuncazometapan.modelos.ordenesactivas.ModeloOrdenesActivas;
import com.tatanstudios.eltuncazometapan.modelos.procesar.ModeloProcesar;
import com.tatanstudios.eltuncazometapan.modelos.producto.ModeloInformacionProducto;
import com.tatanstudios.eltuncazometapan.modelos.productoordenes.ModeloProductosOrdenes;
import com.tatanstudios.eltuncazometapan.modelos.usuario.AccessTokenUser;
import com.tatanstudios.eltuncazometapan.modelos.usuario.ApiRespuesta;
import com.tatanstudios.eltuncazometapan.modelos.zonas.ModeloHorario;
import com.tatanstudios.eltuncazometapan.modelos.zonas.ModeloPoligonoList;
import com.tatanstudios.eltuncazometapan.modelos.zonaservicios.ModeloTipoServiciosZona;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiService {

   /* @GET("v2/list")
    Observable<ModeloPrueba> string_call
            (@Query("page") int page,
            @Query("limit") int limit);*/

   /* @GET("cliente/listado")
    Observable<ModeloPrueba> llamada(
            @Query("page") int page);*/

    // registro de usuario nuevo con area,
    @POST("cliente/registro/v2") //*
    @FormUrlEncoded
    Observable<AccessTokenUser> registroUsuario(@Field("usuario") String usuario,
                                                @Field("password") String password);

    @POST("cliente/login/v2")
    @FormUrlEncoded
    Observable<AccessTokenUser> inicioSesion(@Field("usuario") String usuario,
                                             @Field("password") String password);

    // enviar correo para enviar un codigo
    @POST("cliente/enviar/codigo-correo") //*
    @FormUrlEncoded
    Observable<ApiRespuesta> enviarCodigoCorreo(@Field("correo") String correo);


    // para verificar codigo, cuando se quiere recuperar la contraseña
    @POST("cliente/verificar/codigo-correo-password") //*
    @FormUrlEncoded
    Observable<ApiRespuesta> verificarCodigoCorreoPassword(@Field("correo") String correo,
                                                        @Field("codigo") String codigo);

    @POST("cliente/actualizar/password") // *
    @FormUrlEncoded
    Observable<ApiRespuesta> nuevaPasswordCliente(@Field("id") Integer id,
                                                  @Field("password") String password);


    @FormUrlEncoded
    @POST("cliente/listado/direcciones") // *
    Observable<ModeloDireccion> listadoDeDirecciones(@Field("id") String id);

    @FormUrlEncoded
    @POST("cliente/eliminar/direccion") //*
    Observable<ApiRespuesta> eliminarDireccion(
            @Field("id") String id,
            @Field("dirid") int dirid);


    @FormUrlEncoded
    @POST("cliente/seleccionar/direccion") //*
    Observable<AccessTokenUser> seleccionarDireccion(
            @Field("id") String id,
            @Field("dirid") int dirid);

    // lista de servicios bloque
    @POST("cliente/lista/servicios-bloque/v2") //*
    @FormUrlEncoded
    Observable<ModeloTipoServiciosZona> listaDeTipoServicio(@Field("id") String id);

    @GET("listado/zonas/poligonos") //*
    Observable<ModeloPoligonoList> listadoMapaZonaPoligonos();

    @GET("cliente/eventos/listado") //*
    Observable<ModeloEvento> verEventos();

    @POST("cliente/eventos-imagen/listado") //*
    @FormUrlEncoded
    Observable<ModeloEvento> verEventosImagenes(@Field("id") String id);



    @FormUrlEncoded
    @POST("cliente/nueva/direccion") //*
    Observable<ApiRespuesta> registrarDireccion(
            @Field("id") String id,
            @Field("nombre") String nombre,
            @Field("direccion") String direccion,
            @Field("punto_referencia") String punto_referencia,
            @Field("zona_id") String zonaid,
            @Field("latitud") String latitud,
            @Field("longitud") String longitud,
            @Field("latitudreal") String latitudreal,
            @Field("longitudreal") String longitudreal,
            @Field("telefono") String telefono);

    @POST("cliente/perfil/cambiar-password") //*
    @FormUrlEncoded
    Observable<ApiRespuesta> nuevaPasswordPerfil(@Field("id") String id,
                                                 @Field("password") String password);

    @POST("cliente/informacion") //*
    @FormUrlEncoded
    Observable<AccessTokenUser> infoPerfil(@Field("id") String id);

    @FormUrlEncoded
    @POST("cliente/editar-perfil") //*
    Observable<ApiRespuesta> editarPerfil(@Field("id") String id,

                                          @Field("correo") String correo);
    @FormUrlEncoded
    @POST("cliente/servicios/listado/menu")
    Observable<ModeloMenuVertical> infoLocalMenuVertical(@Field("categoria") int categoria);


    @FormUrlEncoded
    @POST("cliente/informacion/producto") //*
    Observable<ModeloInformacionProducto> infoProductoIndividual(
            @Field("productoid") String productoId);


    @FormUrlEncoded
    @POST("cliente/carrito/producto/agregar") //*
    Observable<ModeloCarritoTemporal> agregarCarritoTemporal(
            @Field("clienteid") String userid,
            @Field("productoid") String productoid,
            @Field("cantidad") int cantidad,
            @Field("notaproducto") String notaproducto);


    @FormUrlEncoded
    @POST("cliente/carrito/ver/orden") //*
    Observable<ModeloCarrito> verCarritoCompras(
            @Field("clienteid") String clienteid);

    @FormUrlEncoded
    @POST("cliente/carrito/borrar/orden") //*
    Observable<ApiRespuesta> borrarCarritoCompras(
            @Field("clienteid") String clienteid);

    @FormUrlEncoded
    @POST("cliente/carrito/eliminar/producto") //**
    Observable<ApiRespuesta> borrarProductoCarrito(@Field("clienteid") String clienteid,
                                                   @Field("carritoid") int carritoid);

    @FormUrlEncoded
    @POST("cliente/carrito/ver/producto")
    Observable<ModeloCarritoProductoEditar> infoProductoIndividualCarrito(
            @Field("clienteid") String userid, @Field("carritoid") int carritoid);


    @FormUrlEncoded
    @POST("cliente/carrito/cambiar/cantidad")
    Observable<ApiRespuesta> cambiarCantidadProducto(
            @Field("clienteid") String clienteid, @Field("cantidad") int cantidad,
            @Field("carritoid") int carritoid, @Field("nota") String nota);

    @FormUrlEncoded
    @POST("cliente/carrito/ver/proceso-orden")
    Observable<ModeloProcesar> verProcesarOrden(
            @Field("clienteid") String clienteid);


    @FormUrlEncoded
    @POST("cliente/proceso/orden/estado-1")
    Observable<ApiRespuesta> enviarOrden(
            @Field("clienteid") String clienteid,
            @Field("nota") String nota,
            @Field("version") String dispositivo);


    @FormUrlEncoded
    @POST("cliente/ver/ordenes-activas") //
    Observable<ModeloOrdenesActivas> verOrdenesActivas(
            @Field("clienteid") String clienteid);


    @FormUrlEncoded
    @POST("cliente/ver/estado-orden") //
    Observable<ModeloOrdenesActivas> verOrdenId(
            @Field("ordenid") String ordenid);


    @FormUrlEncoded
    @POST("cliente/listado/productos/ordenes") //
    Observable<ModeloProductosOrdenes> verProductosOrdenes(
            @Field("ordenid") String ordenid);


    @FormUrlEncoded
    @POST("cliente/listado/productos/ordenes-individual") //
    Observable<ModeloProductosOrdenes> verProductosOrdenesIndividual(
            @Field("productoid") String productoid);


    @FormUrlEncoded
    @POST("cliente/proceso/orden/cancelar") //
    Observable<ApiRespuesta> cancelarOrden(
            @Field("ordenid") String ordenid);


    @FormUrlEncoded
    @POST("cliente/proceso/calificar/entrega")
    Observable<ApiRespuesta> calificarOrden(
            @Field("ordenid") String ordenid,
            @Field("valor") int estrellas,
            @Field("mensaje") String mensaje);

    @FormUrlEncoded
    @POST("cliente/ver/historial")
    Observable<ModeloHistorial> verHistorial(
            @Field("id") String id,
            @Field("fecha1") String fecha1,
            @Field("fecha2") String fecha2);


    @FormUrlEncoded
    @POST("cliente/ver/productos/historial") //
    Observable<ModeloProductosOrdenes> verProductosHistorial(
            @Field("ordenid") String ordenid);

    @GET("cliente/horarios")
    Observable<ModeloHorario> verHorarios();


    @FormUrlEncoded
    @POST("cliente/opcion/carrito/compras")
    Observable<ModeloInformacion> verOpcionCarrito(
            @Field("id") String id);


    @FormUrlEncoded
    @POST("cliente/opcion/carrito/guardar")
    Observable<ModeloInformacion> guardarOpcionCarrito(
            @Field("id") String id,
            @Field("disponible") int toggle);









}
