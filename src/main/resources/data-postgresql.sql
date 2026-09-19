-- Datos iniciales para PostgreSQL (Render.com)
-- Solo incluye columnas que existen en la entidad Producto

INSERT INTO productos (id, nombre, cantidad, precio, categoria, codigo)
VALUES
(1, 'Mouse inalámbrico', 20, 65000, 'Tecnologia', 'P001'),
(4, 'Audífonos', 12, 80000, 'Tecnología', 'P002'),
(5, 'pantalla', 20, 26900, 'Tecnología', 'P003'),
(6, 'llaves', 0, 20000, 'Accesorios', 'P004'),
(7, 'papel', 100, 45000, 'Papelería', 'P005')
ON CONFLICT (id) DO NOTHING;

-- Ajustar secuencia para que los próximos IDs continúen correctamente
SELECT setval(pg_get_serial_sequence('productos', 'id'), COALESCE(MAX(id), 1)) FROM productos;
