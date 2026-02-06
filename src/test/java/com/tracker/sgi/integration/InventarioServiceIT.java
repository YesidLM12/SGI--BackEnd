package com.tracker.sgi.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tracker.sgi.SgiApplication;
import com.tracker.sgi.config.SecurityConfig;
import com.tracker.sgi.entities.Categorias;
import com.tracker.sgi.entities.Proveedores;
import com.tracker.sgi.repository.CategoriaRepository;
import com.tracker.sgi.repository.ProveedoresRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.tracker.sgi.entities.MovimientoInventario;
import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.repository.MovimientoInventarioRepository;
import com.tracker.sgi.repository.ProductoRepository;
import com.tracker.sgi.service.InventarioService;
import com.tracker.sgi.util.enums.TipoMovimientoEnum;

import jakarta.transaction.Transactional;

import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@SpringBootTest(classes = SgiApplication.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(exclude = {
        SecurityConfig.class
})
@Transactional
class InventarioServiceIT {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProveedoresRepository proveedoresRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Autowired
    EntityManager em;

    @Test
    void verificarTablas() {
        em.createNativeQuery("SHOW TABLES")
                .getResultList()
                .forEach(System.out::println);
    }

    @Test
    void verificarEntidadesCargadas() {
        em.getMetamodel().getEntities()
                .forEach(e -> System.out.println(e.getName()));
    }

    @Test
    void debeRegistrarSalidaYActualizarStockEnBD() {

        // Arrange: Producto real en BD
        Categorias categoria = new Categorias();
        categoria.setNombre("Periféricos");
        categoriaRepository.save(categoria);

        Proveedores proveedor = new Proveedores();
        proveedor.setNombre("Logitech");
        proveedoresRepository.save(proveedor);

        Productos producto = new Productos();
        producto.setNombre("Teclado");
        producto.setStock_actual(10);
        producto.setStock_minimo(2);
        producto.setPrecio(BigDecimal.valueOf(100));
        producto.setDisponible(true);
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);
        productoRepository.saveAndFlush(producto);

        // Act: Operación real
        inventarioService.registrarMovimiento(producto, TipoMovimientoEnum.ENTRADA, 10);

        MovimientoInventario salida =inventarioService.registrarMovimiento(producto, TipoMovimientoEnum.SALIDA, 3);

        // Assert: movimiento guardado correctamente
        assertEquals(7, salida.getStock_resultante());

        // Assert: producto actualizado correctamente
        Productos productoActualizado = productoRepository.findById(producto.getId())
        .orElseThrow();
        assertEquals(7, productoActualizado.getStock_actual());
        assertEquals(2, movimientoInventarioRepository.count());
    }

}
