-- Datos iniciales para PostgreSQL (Render.com)
-- Spring Boot carga este archivo automáticamente cuando platform=postgresql

INSERT INTO productos (id, nombre, descripcion, cantidad, precio, categoria, codigo, marca)
VALUES
(1, 'Mouse inalámbrico', 'Mouse óptico inalámbrico', 20, 65000, 'Tecnologia', 'P001', NULL),
(4, 'Audífonos', NULL, 12, 80000, 'Tecnología', 'P002', NULL),
(5, 'pantalla', NULL, 20, 26900, 'Tecnología', 'P003', NULL),
(6, 'llaves', NULL, 0, 20000, 'Accesorios', 'P004', NULL),
(7, 'papel', NULL, 100, 45000, 'Papelería', 'P005', NULL)
ON CONFLICT (id) DO NOTHING;

-- Ajustar secuencia para que los próximos IDs continúen correctamente
SELECT setval(pg_get_serial_sequence('productos', 'id'), COALESCE(MAX(id), 1)) FROM productos;
