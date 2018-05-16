package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/core-config.xml"})
public class ContentControlAlfrescoTest {

    private static final Logger logger = LogManager.getLogger(ContentControlAlfresco.class.getName());
    @Autowired
    private ContentControlAlfresco contentControlAlfresco;


    private DocumentoDTO documentoDTO;
    private MensajeRespuesta mensajeRespuesta;
    private Conexion conexion;
    private MensajeRespuesta mensajeRespuesta1;
    private DocumentoDTO documentoDTO1;
    private DocumentoDTO documentoDTO2;
    private ContenidoDependenciaTrdDTO dependenciaTrdDTO;
    private UnidadDocumentalDTO unidadDocumentalDTO;

    @Before
    public void Setup() {
        conexion = new Conexion();
        mensajeRespuesta = new MensajeRespuesta();
        //llenar documento
        documentoDTO = new DocumentoDTO();
        documentoDTO.setTipoDocumento("application/pdf");
        documentoDTO.setNombreDocumento("DocTestJUnit");
        documentoDTO.setVersionLabel("1.0");
        documentoDTO.setNombreRemitente("UserTest");
        documentoDTO.setNroRadicado("1234567");
        documentoDTO.setTipologiaDocumental("Principal");
        String documento = "JVBERi0xLjMNJeLjz9MNCjcgMCBvYmoNPDwvTGluZWFyaXplZCAxL0wgNzk0NS9PIDkvRSAzNTI0L04gMS9UIDc2NTYvSCBbIDQ1MSAxMzddPj4NZW5kb2JqDSAgICAgICAgICAgICAgICAgICAgICAgDQoxMyAwIG9iag08PC9EZWNvZGVQYXJtczw8L0NvbHVtbnMgNC9QcmVkaWN0b3IgMTI+Pi9GaWx0ZXIvRmxhdGVEZWNvZGUvSURbPDREQzkxQTE4NzVBNkQ3MDdBRUMyMDNCQjAyMUM5M0EwPjxGNkM5MkIzNjhBOEExMzQwODQ1N0ExRDM5NUEzN0VCOT5dL0luZGV4WzcgMjFdL0luZm8gNiAwIFIvTGVuZ3RoIDUyL1ByZXYgNzY1Ny9Sb290IDggMCBSL1NpemUgMjgvVHlwZS9YUmVmL1dbMSAyIDFdPj5zdHJlYW0NCmjeYmJkEGBgYmCyARIMIIKxAUgwpwIJNkcg8eUYAxMjwzSQLAMjucR/xp1fAAIMAEykBvANCmVuZHN0cmVhbQ1lbmRvYmoNc3RhcnR4cmVmDQowDQolJUVPRg0KICAgICAgICANCjI3IDAgb2JqDTw8L0ZpbHRlci9GbGF0ZURlY29kZS9JIDY5L0xlbmd0aCA1OC9TIDM4Pj5zdHJlYW0NCmjeYmBgYGFgYPzPAATcNgyogJEBJMvRgCzGAsUMDA0M3Azc0x50JoA4zAwMWgIQLYwsAAEGAL/iBRkNCmVuZHN0cmVhbQ1lbmRvYmoNOCAwIG9iag08PC9NZXRhZGF0YSAxIDAgUi9QYWdlcyA1IDAgUi9UeXBlL0NhdGFsb2c+Pg1lbmRvYmoNOSAwIG9iag08PC9Db250ZW50cyAxMSAwIFIvQ3JvcEJveFswIDAgNTk1IDg0Ml0vTWVkaWFCb3hbMCAwIDU5NSA4NDJdL1BhcmVudCA1IDAgUi9SZXNvdXJjZXMgMTQgMCBSL1JvdGF0ZSAwL1R5cGUvUGFnZT4+DWVuZG9iag0xMCAwIG9iag08PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgOTQvTGVuZ3RoIDc3My9OIDEzL1R5cGUvT2JqU3RtPj5zdHJlYW0NCmjevFRtb9owEP4r/gPgl9hxIlVI0I6u0lqhJls/RPmQgguRQoISV6P/fncJLoG1K6XSiMz55e58vue545IwwhXhnibcJyKAlSaeCAgPiOeDCImUighGVMiI4CQUoCYIZ1oS4YGt5kRIsGIhEeAokLAGFcYkubigl1VR1dEmmxtcNAovY+R+NKLftvY6spnFg+uI4/XdwbQqLexNBcYAWzSOBQbQTSXe3k19vLibBnhnZz6rq3lkbEJnV1Mam61NR6OEXmbF/fUEr8rW6ywRQwE/iPRQpvQ2s3W+TdhQcnQ+FBwdDxkPPRCe0rjSXEFe2JDzUKAImEIdjZENQ8VUSh9WuTWzKi9t0m0ReOGQBSFEk0IY0Zg8ZUVjaHSLpoLG9/RmYUqb2xcav2zMPj+jEehf5U9Ppjbl3DQJp4/PRWFsulMs59UiL5et3iRrDCaQRi/rx6p4PURYMVXR86NFI7TkNK5+ljkoGMJ3ScUztG+djZs5RERCpiB/m+8mX64sYfTKdPsDwTmdFtmyAca0VpNJtU0GPtBn4GkkgQfMYDJI29O7bG3ouM6zYjCpisVtTG9sVuTzcbksDPiNrFn/Aip6+zDwqjrf2Ko+fN2BF/dG+pCX47LJX9fTvG7s5SqrXXx7d0hsfPCPbKfBub9PTv1sYpel1hBcL+yqSYRGSn7ta2nyKn3O39Dxff2hH6X81rovuxMXpZPuDi8IWy3P89I+wEHI3wPYdwDLHsDKR4CZBoCxUzCmewDH+do0d+b3fbXOyln0DsrsY4z/dnQW0IIfAa3lKUCrw2RDjWPa2tGmVu3/T4UcQe1me6iOAXXQO8hCKd/QlLCr2KHEyHCOo08ADcPt49i9A6ggeie7uBgj/+vTPku/1GV8BSQUypHQ08dd5nzqOfPzCOcdEg40Tmosny3JMOiXpNRdSXLBfMyGeL8k277ZZeYoRQOuPtOF/+n3vNypo2IV/Ixi3X+nFuipPfeDjsxccbr/rqgP+zHu9IoRCtEVo4tiV9JAiD8CDAA+0IrxDQplbmRzdHJlYW0NZW5kb2JqDTExIDAgb2JqDTw8L0ZpbHRlci9GbGF0ZURlY29kZS9MZW5ndGggMTUzMD4+c3RyZWFtDQpIibRXS2/jNhBGr/4Vc1uqiBW9H8d0tynQ02IroIduD7LEJCpk0RDppPlT/Y2dB2l7nS0KLFoEUPgacuabmW/GP3Sb267LIIXuYZMWcVJAgn8yytI8rqukgrqscZ7k0O03t+9tCYPlYwnYYXP70y8pPNrNNomTJKugGzY0qhroXja/qbsoTeJMjdG2jlNldhqibUpD3GjiWg3RNlNrtK3iCnd7Bx8/3MP9RAuNmrWNfu9+Jh0Lr2MmCmbQtHGbkXJZG+eZKMc6JK3XIaMR6zDiu3/BR7O6fjdr+GBQhyRu1XDc68XBfVTGucJFWlv3uJmjgqjLZ4Xa8ObnCCZLqieqh+MyPevV9rMsPEwzWZXhyKx7FONV9xRGh5WMb5W2en32L+sow2+4cZ7ZzAS2aZyW0H1gCJPGG9K2mRhiHqIcYYGI79dRgaDxRNbN4uzN5TxK8LvymKyKC9WzjHPTEm1b9MsjuadRN3ySRQc+IaKzOYq05S0RXkZ4lFWZH54mkbFRosDIvV5RL8GXvcpTYrLFm0XKWzEamR5JUdJUX4i6G5AXdbQtcc9r3dMs9waOorGIWQuIFWHafe+jogiRSSMCEwGE/nCYp6F3k1mgR8MOc+/IiXC0rEam9AjOwLBqCdEe3yqU0zC5OPgsi3PvspTC8BRxjJkEUCvYTh7HRWYjX1rypaWaxXMSQg8Somgc6NkfG/iYW80yDYQXQ5XhEsXwOFm3TrujmGJRPzAYpIPZawsUK1cBJqDUJ1BqUfywGsyQvQUU3Jtl5hda8h1mmQK9sFqYtua4OM2BXRNGL5N7Ik0HVs9LDcCpYZ96MgBTC4M+V9PyGNFlgt/tvWcfAbJhJFkrUkh9F3V/UPpX/lBcVJj+eAYBlZ3GE4NwV0id0htWtSXfc7e8mkXfoJNfX540elOEPaugEV6YYUm9cJ0KKDCgx8xBI7BIT9G2wUAjr2aKDYzhbiYqyBPGSZmjxPiiCR4OIZ4HAqHAE+JA/DCm/YxihoJOhfmw+oUeccMkYLy2rCu5sQjGpj6006SpROFPmrXr+TtGkk40XjE7ChVzpH3SA69NxHuNOkxyZOHjTiIVk4gEZExRdL7E8wwNEQOPBk8N3yCn9nK5aOJkYsFiVMrK5AcYcBcqL4Rxpd5FmIJVEEMPyPKlnvClBhZ2+vKiIx+yXj0yYIu1jbjoq+nwhiNGs7zDYEXw4akX7iYoiQPgzB+eGij1LDLHP1EGCZzTtqK0tVdJgPqU35gHxdfyQEJjG4ZkEhFSTYx7jVyotD6hsAUoLy4qzxeVclE/v/SvXByR+JEF4LBOSESDL6ZoiVpXzTNZc/PrVTXHRGov8i7JTvj7ggfMy1RbUUUmoca/MwkTUQXjxVE/iyPEP/U1vZDfi+K/xDb0GWndppfQpgRtjnQ3cTGqEdqe/xOZIgwvyIYp4fEaZdQKEHoogwSO1efLrWufUOvwluXkcS6NtfqzH97inF3hHDRvQ4dEFYNJh6OWbOi5QXF6pNIr7YtsEN5hex1n3yz5fobKLtYu7kOseXBkKwmtTL2jMBgKNPmZwr5MvSqkHvLt2gc3F/ysb3awNGdpiAes9Q7rlVAakfJlG0QlXQTZBmx/qFkJzQxnJ9WkSkmtXoyD2VgspkdNKRy6gbMtLIG2SNvmDbpq29LsnCo+jJ8xDZgQM/Y2Zh3G9bRgWnCiZGp/QL5CNtxN8+SIiNX/yQzbs5oUvkHLDvnpQfyPSQR3g4xWbss/6X4MLdFKvbA/1zN+5BJ2CJVGgm40L8ts+pG7KoksrKG7U+ELr2D8ZESPQfTUxiCJ7i5Z+hwqeXMR9UQOFE90QYW6YdtEs7CqsSX9dyC/mV1zgbBoGt8+vTfsSYz4gb9OflOcOsEaSfFUOHNPvumpvabxKnksG2D3sjr7kyvLYSmRZSqCPKXKGIQm/0NGjlKnzaPBX3n9tL9p9D6Tm2QR3fdVF4SI4ah9pHAFjl9EXUYghV0eY680/EukCF0CF2hl3QXtEelReBHnc6uh4Ff67sSBP3abvwcArRiH3QoNCmVuZHN0cmVhbQ1lbmRvYmoNMTIgMCBvYmoNPDwvRmlsdGVyL0ZsYXRlRGVjb2RlL0xlbmd0aCAyMDg+PnN0cmVhbQ0KSIlUkL0OwjAMhPc+hUcQQ9rOVRdYOvAjCuxp4laRiBO56dC3JykFxBBL9uXTnS32zaEhE0Bc2KkWA/SGNOPoJlYIHQ6GoChBGxXWbqnKSg8iwu08BrQN9Q6qKhPXKI6BZ9i0s+3cc5dvQZxZIxsaYHMr7o84aCfvn2iRAuRQ16Cxz8T+KP1JWozyii7zYjV0GkcvFbKkAaHKi/pdkPS/9iG6/t3+vlZlXpZ1FomPluC0yddbTcwx1rLukihlMITfi3jnk2V62UuAAQBDyGk/Cg0KZW5kc3RyZWFtDWVuZG9iag0xIDAgb2JqDTw8L0xlbmd0aCAzNjU2L1N1YnR5cGUvWE1ML1R5cGUvTWV0YWRhdGE+PnN0cmVhbQ0KPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4KPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNC4yLjEtYzA0MyA1Mi4zNzI3MjgsIDIwMDkvMDEvMTgtMTU6MDg6MDQgICAgICAgICI+CiAgIDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+CiAgICAgIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiCiAgICAgICAgICAgIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyI+CiAgICAgICAgIDxkYzpmb3JtYXQ+YXBwbGljYXRpb24vcGRmPC9kYzpmb3JtYXQ+CiAgICAgICAgIDxkYzpjcmVhdG9yPgogICAgICAgICAgICA8cmRmOlNlcT4KICAgICAgICAgICAgICAgPHJkZjpsaT5jZGFpbHk8L3JkZjpsaT4KICAgICAgICAgICAgPC9yZGY6U2VxPgogICAgICAgICA8L2RjOmNyZWF0b3I+CiAgICAgICAgIDxkYzp0aXRsZT4KICAgICAgICAgICAgPHJkZjpBbHQ+CiAgICAgICAgICAgICAgIDxyZGY6bGkgeG1sOmxhbmc9IngtZGVmYXVsdCI+VGhpcyBpcyBhIHRlc3QgUERGIGZpbGU8L3JkZjpsaT4KICAgICAgICAgICAgPC9yZGY6QWx0PgogICAgICAgICA8L2RjOnRpdGxlPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgICAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgICAgICAgICAgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIj4KICAgICAgICAgPHhtcDpDcmVhdGVEYXRlPjIwMDAtMDYtMjlUMTA6MjE6MDgrMTE6MDA8L3htcDpDcmVhdGVEYXRlPgogICAgICAgICA8eG1wOkNyZWF0b3JUb29sPk1pY3Jvc29mdCBXb3JkIDguMDwveG1wOkNyZWF0b3JUb29sPgogICAgICAgICA8eG1wOk1vZGlmeURhdGU+MjAxMy0xMC0yOFQxNToyNDoxMy0wNDowMDwveG1wOk1vZGlmeURhdGU+CiAgICAgICAgIDx4bXA6TWV0YWRhdGFEYXRlPjIwMTMtMTAtMjhUMTU6MjQ6MTMtMDQ6MDA8L3htcDpNZXRhZGF0YURhdGU+CiAgICAgIDwvcmRmOkRlc2NyaXB0aW9uPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczpwZGY9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGRmLzEuMy8iPgogICAgICAgICA8cGRmOlByb2R1Y2VyPkFjcm9iYXQgRGlzdGlsbGVyIDQuMCBmb3IgV2luZG93czwvcGRmOlByb2R1Y2VyPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgICAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgICAgICAgICAgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iPgogICAgICAgICA8eG1wTU06RG9jdW1lbnRJRD51dWlkOjA4MDVlMjIxLTgwYTgtNDU5ZS1hNTIyLTYzNWVkNWMxZTJlNjwveG1wTU06RG9jdW1lbnRJRD4KICAgICAgICAgPHhtcE1NOkluc3RhbmNlSUQ+dXVpZDo2MmQ2YWU2ZC00M2M0LTQ3MmQtOWIyOC03YzRhZGQ4ZjllNDY8L3htcE1NOkluc3RhbmNlSUQ+CiAgICAgIDwvcmRmOkRlc2NyaXB0aW9uPgogICA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgCjw/eHBhY2tldCBlbmQ9InciPz4NCmVuZHN0cmVhbQ1lbmRvYmoNMiAwIG9iag08PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgNC9MZW5ndGggNDgvTiAxL1R5cGUvT2JqU3RtPj5zdHJlYW0NCmjeMlUwULCx0XfOL80rUTDU985MKY62BIoFxeqHVBak6gckpqcW29kBBBgA1ncLgA0KZW5kc3RyZWFtDWVuZG9iag0zIDAgb2JqDTw8L0ZpbHRlci9GbGF0ZURlY29kZS9GaXJzdCA0L0xlbmd0aCAxNjcvTiAxL1R5cGUvT2JqU3RtPj5zdHJlYW0NCmjePMvBCsIwEEXRX5mdDaKdxCpVSqFY3AkuBNexSelA6EAyRfx7A4qPu3znAAhNU3aLTByLwVkKb1Weo7dCPPdWfNGfDOYdzFGj0VivtV4hrn6vrK40RE48Cjw4Oqi3qMoruz/WuwxrvTeV3m2w+uJbZLcMPhZdxk8r0FMSCsFHqLYII0d40Oz4lVR5Jwm+uE+UIGdBfBK49RcYKXjVth8BBgBnZztkDQplbmRzdHJlYW0NZW5kb2JqDTQgMCBvYmoNPDwvRGVjb2RlUGFybXM8PC9Db2x1bW5zIDMvUHJlZGljdG9yIDEyPj4vRmlsdGVyL0ZsYXRlRGVjb2RlL0lEWzw0REM5MUExODc1QTZENzA3QUVDMjAzQkIwMjFDOTNBMD48RjZDOTJCMzY4QThBMTM0MDg0NTdBMUQzOTVBMzdFQjk+XS9JbmZvIDYgMCBSL0xlbmd0aCAzNy9Sb290IDggMCBSL1NpemUgNy9UeXBlL1hSZWYvV1sxIDIgMF0+PnN0cmVhbQ0KaN5iYmBgYGLkPcLEwD+ViYGhh4mBkYWJ8bEkkM0IEGAAKlkDFA0KZW5kc3RyZWFtDWVuZG9iag1zdGFydHhyZWYNCjExNg0KJSVFT0YNCg==";
        documentoDTO.setDocumento(documento.getBytes());
        documentoDTO.setSede("1000_VICEPRESIDENCIA ADMINISTRATIVA");
        documentoDTO.setDependencia("1000.1040_GERENCIA NACIONAL DE GESTION DOCUMENTAL");
        documentoDTO.setCodigoSede("1000");
        documentoDTO.setCodigoDependencia("10001040");

        //
        //crear conexion


        //Inicializar coneccion
        Map<String, String> parameter = new HashMap<>();
        // Credenciales del usuario
        parameter.put(SessionParameter.USER, "admin");
        parameter.put(SessionParameter.PASSWORD, "admin");

        // configuracion de conexion
        parameter.put(SessionParameter.ATOMPUB_URL, "http://192.168.3.245:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom");
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.REPOSITORY_ID, "-default-");

        // Object factory de Alfresco
        parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

        // Crear Sesion
        SessionFactory factory = SessionFactoryImpl.newInstance();
        conexion.setSession(factory.getRepositories(parameter).get(0).createSession());
        //Se crea el documento

        mensajeRespuesta = contentControlAlfresco.subirDocumentoPrincipalAdjunto(conexion.getSession(), documentoDTO, "EE");
        documentoDTO.setIdDocumento(mensajeRespuesta.getDocumentoDTOList().get(0).getIdDocumento());
        //Crear documentoDTO diferente
        documentoDTO1 = new DocumentoDTO();
        documentoDTO1.setTipoDocumento("application/pdf");
        documentoDTO1.setNombreDocumento("TestMetodoSubirDoc1");
        documentoDTO1.setVersionLabel("1.0");
        documentoDTO1.setNombreRemitente("UserTest");
        documentoDTO1.setNroRadicado("1234567");
        documentoDTO1.setTipologiaDocumental("Principal");
        documentoDTO1.setDocumento(documento.getBytes());
        documentoDTO1.setSede("1000_VICEPRESIDENCIA ADMINISTRATIVA");
        documentoDTO1.setDependencia("1000.1040_GERENCIA NACIONAL DE GESTION DOCUMENTAL");
        documentoDTO1.setCodigoSede("1000");
        documentoDTO1.setCodigoDependencia("10001040");
        //Crear documentoDTO diferente
        documentoDTO2 = new DocumentoDTO();
        documentoDTO2.setTipoDocumento("application/pdf");
        documentoDTO2.setNombreDocumento("TestMetodoSubirDoc2");
        documentoDTO2.setVersionLabel("1.0");
        documentoDTO2.setNombreRemitente("UserTest");
        documentoDTO2.setNroRadicado("1234567");
        documentoDTO2.setTipologiaDocumental("Principal");
        documentoDTO2.setDocumento(documento.getBytes());
        documentoDTO2.setSede("1000_VICEPRESIDENCIA ADMINISTRATIVA");
        documentoDTO2.setCodigoSede("1000");
        documentoDTO2.setDependencia("1000.1040_GERENCIA NACIONAL DE GESTION DOCUMENTAL");
        documentoDTO2.setCodigoDependencia("10001040");


        //Se crea el objeto que contiene la dependencia de prueba dependenciaTrdDTO
        dependenciaTrdDTO = new ContenidoDependenciaTrdDTO();
        dependenciaTrdDTO.setIdOrgAdm("1000");
        dependenciaTrdDTO.setIdOrgOfc("10001010");

        //Se llenan los datos de la unidad documental
        unidadDocumentalDTO = new UnidadDocumentalDTO();
        unidadDocumentalDTO.setInactivo(true);
        //Calendar calendar
        Calendar gregorianCalendar = GregorianCalendar.getInstance();
        unidadDocumentalDTO.setFechaCierre(gregorianCalendar);
        unidadDocumentalDTO.setId("1118");
        unidadDocumentalDTO.setFechaExtremaInicial(gregorianCalendar);
        unidadDocumentalDTO.setSoporte("electronico");
        unidadDocumentalDTO.setNombreUnidadDocumental("UnidadDocumentalTest");
        unidadDocumentalDTO.setFechaExtremaFinal(gregorianCalendar);
        unidadDocumentalDTO.setCerrada(true);
        unidadDocumentalDTO.setCodigoSubSerie("02312");
        unidadDocumentalDTO.setCodigoSerie("0231");
        unidadDocumentalDTO.setCodigoDependencia("10001040");
        unidadDocumentalDTO.setDescriptor1("3434");
        unidadDocumentalDTO.setDescriptor2("454545");
        unidadDocumentalDTO.setAccion("ABRIR");
        unidadDocumentalDTO.setInactivo(false);
        unidadDocumentalDTO.setCerrada(false);
        unidadDocumentalDTO.setEstado("Abierto");
        unidadDocumentalDTO.setDisposicion("SSS");

    }

    @After
    public void afterFunct() {
        contentControlAlfresco.eliminardocumento(mensajeRespuesta.getDocumentoDTOList().get(0).getIdDocumento(), conexion.getSession());
        try {
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession());
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDescargarDocumentoSuccess() {
        //Prueba Existosa para descargar documento
        try {
            assertEquals("0000", contentControlAlfresco.
                    descargarDocumento(mensajeRespuesta.getDocumentoDTOList().get(0), conexion.getSession()).getCodMensaje());

            //Prueba para descargar documento que no existe
            DocumentoDTO documentoDTO2 = new DocumentoDTO();
            documentoDTO2.setIdDocumento("sdasdasdasd");
            assertEquals("2222", contentControlAlfresco.
                    descargarDocumento(documentoDTO2, conexion.getSession()).getCodMensaje());
        } catch (Exception e) {
            logger.error("Ocurrio un error en el Servidor", e);
        }

    }

    @Test
    public void testDevolverSerieSubSerieSuccess() {
        //Prueba Existosa para devolver serie subserie
        try {
            assertEquals("0000", contentControlAlfresco.
                    devolverSerieSubSerie(dependenciaTrdDTO, conexion.getSession()).getCodMensaje());
        } catch (Exception e) {
            logger.error("Ocurrio un error en el Servidor", e);
        }
        //Prueba para cuadno se pasa vacio el objeto contenidoDependenciaTrdDTO
        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO = new ContenidoDependenciaTrdDTO();
        try {
            contentControlAlfresco.devolverSerieSubSerie(contenidoDependenciaTrdDTO, conexion.getSession());
        } catch (Exception e) {
            assertEquals("No se ha especificado el codigo de la dependencia", e.getMessage());
            logger.error("Ocurrio un error en el Servidor", e);
        }
    }

    @Test
    public void testCrearUnidadDocumentalSuccess() {
        //Crear unidad documental
        try {
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                    crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());
            assertEquals("0000", mensajeRespuesta.getCodMensaje());
            UnidadDocumentalDTO unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarUnidadDocumentalSuccess() {
        try {
            assertEquals("0000", contentControlAlfresco.
                    listarUnidadDocumental(unidadDocumentalDTO, conexion.getSession()).getCodMensaje());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testListarUnidadDocumentalSinAccionSuccess(){
        //Se llenan los datos de la unidad documental
        UnidadDocumentalDTO unidadDocumentalDTO1 = new UnidadDocumentalDTO();
        unidadDocumentalDTO1.setInactivo(true);
        //Calendar calendar
        Calendar gregorianCalendar = GregorianCalendar.getInstance();
        unidadDocumentalDTO1.setFechaCierre(gregorianCalendar);
        unidadDocumentalDTO1.setId("1118");
        unidadDocumentalDTO1.setFechaExtremaInicial(gregorianCalendar);
        unidadDocumentalDTO1.setSoporte("electronico");
        unidadDocumentalDTO1.setNombreUnidadDocumental("UnidadDocumentalTest");
        unidadDocumentalDTO1.setFechaExtremaFinal(gregorianCalendar);
        unidadDocumentalDTO1.setCerrada(true);
        unidadDocumentalDTO1.setCodigoSubSerie("02312");
        unidadDocumentalDTO1.setCodigoSerie("0231");
        unidadDocumentalDTO1.setCodigoDependencia("10001040");
        unidadDocumentalDTO1.setDescriptor1("3434");
        unidadDocumentalDTO1.setDescriptor2("454545");
        unidadDocumentalDTO1.setAccion("");
        unidadDocumentalDTO1.setInactivo(false);
        unidadDocumentalDTO1.setCerrada(false);
        unidadDocumentalDTO1.setEstado("Abierto");
        unidadDocumentalDTO1.setDisposicion("SSS");

        //Crear unidad documental
        try {
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                    crearUnidadDocumental(unidadDocumentalDTO1, conexion.getSession());
            UnidadDocumentalDTO unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");

            assertEquals("0000", contentControlAlfresco.
                    listarUnidadDocumental(unidadDocumentalDTO, conexion.getSession()).getCodMensaje());
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testObtenerDetallesDocumentoDTOSuccess() {

        //Prueba Existosa para obtenerdetalles de documento
        try {
            assertEquals("0000", contentControlAlfresco.
                    obtenerDetallesDocumentoDTO(mensajeRespuesta.getDocumentoDTOList().get(0).
                            getIdDocumento(), conexion.getSession()).getCodMensaje());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Prueba cuadno viene vacio el idDocumento
        try {
            assertEquals("11111", contentControlAlfresco.
                    obtenerDetallesDocumentoDTO(null, conexion.getSession()).getCodMensaje());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDetallesUnidadDocumentalSuccess() {
        //Crear unidad documental
        try {
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                    crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());

            UnidadDocumentalDTO unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            //Listar detalles
            assertEquals("0000", contentControlAlfresco.
                    detallesUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession()).getCodMensaje());
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession());
        } catch (SystemException e) {
            logger.error("Error SystemException: {}", e);
        } catch (Exception e) {
            logger.error("Error Exception: {}", e);
        }
    }

    @Test
    public void testSubirDocumentoPrincipalAdjuntoEESuccess() {
        //Probar que sube documento EE correctemante
        mensajeRespuesta1 = contentControlAlfresco.subirDocumentoPrincipalAdjunto(conexion.getSession(), documentoDTO1, "EE");
        assertEquals("0000", mensajeRespuesta.getCodMensaje());
        contentControlAlfresco.eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento(), conexion.getSession());
    }

    @Test
    public void testSubirDocumentoPrincipalAdjuntoEISuccess() {
        //Probar que sube documento EI correctemante
        mensajeRespuesta1 = contentControlAlfresco.
                subirDocumentoPrincipalAdjunto(conexion.getSession(), documentoDTO1, "EI");
        assertEquals("0000", mensajeRespuesta.getCodMensaje());
        contentControlAlfresco.
                eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento(), conexion.getSession());
    }

    @Test
    public void testSubirDocumentoPrincipalAdjuntoSISuccess() {
        //Probar que sube documento EE correctemante
        mensajeRespuesta1 = contentControlAlfresco.subirDocumentoPrincipalAdjunto(conexion.getSession(), documentoDTO1, "SI");
        assertEquals("0000", mensajeRespuesta.getCodMensaje());
        contentControlAlfresco.eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento(), conexion.getSession());
    }

    @Test
    public void testSubirDocumentoPrincipalAdjuntoDefaultSuccess() {
        //Probar que sube documento EI correctemante
        mensajeRespuesta1 = contentControlAlfresco.
                subirDocumentoPrincipalAdjunto(conexion.getSession(), documentoDTO1, "OTHER");
        assertEquals("0000", mensajeRespuesta.getCodMensaje());
        contentControlAlfresco.
                eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento(), conexion.getSession());
    }

    @Test
    public void testSubirDocumentoPrincipalAdjuntoSESuccess() {
        //Probar que sube documento EI correctemante
        mensajeRespuesta1 = contentControlAlfresco.
                subirDocumentoPrincipalAdjunto(conexion.getSession(), documentoDTO1, "SE");
        assertEquals("0000", mensajeRespuesta.getCodMensaje());
        contentControlAlfresco.
                eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento(), conexion.getSession());
    }

    @Test
    public void testSubirDocumentoPrincipalAdjuntoPDSuccess() {
        //Probar que sube documento PD correctemante
        mensajeRespuesta1 = contentControlAlfresco.
                subirDocumentoPrincipalAdjunto(conexion.getSession(), documentoDTO2, "PD");
        assertEquals("0000", mensajeRespuesta.getCodMensaje());
        contentControlAlfresco.
                eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento(), conexion.getSession());
    }

    @Test
    public void testGetUDByIdSuccess() throws Exception {
        try {
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                    crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());

            UnidadDocumentalDTO unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            //Obtener la unidad documental

            final Optional<UnidadDocumentalDTO> optionalDocumentalDTO = contentControlAlfresco.
                    getUDById(unidadDocumentalDTO.getId(), true, conexion.getSession());

            optionalDocumentalDTO.ifPresent(unidadDocumentalDTO1 ->
                    assertNotNull(unidadDocumentalDTO1.getId()));

            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession());

        } catch (SystemException e) {
            logger.error("Error: {}", e);
        } catch (Exception e) {
            logger.error("Error: {}", e);
        }
    }

    @Test
    public void testEliminardocumentoTrueSuccess() {
        mensajeRespuesta1 = contentControlAlfresco.subirDocumentoPrincipalAdjunto(conexion.getSession(), documentoDTO1, "EE");
        //Probar documento se sube correctamente
        assertEquals("0000", mensajeRespuesta1.getCodMensaje());
        assertTrue(contentControlAlfresco.eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento(), conexion.getSession()));
    }

    @Test
    public void testEliminardocumentoFalseSuccess() {
        DocumentoDTO documentoDTO13 = new DocumentoDTO();
        documentoDTO13.setIdDocumento("WEWEWE");
        assertFalse(contentControlAlfresco.eliminardocumento(documentoDTO13.getIdDocumento(), conexion.getSession()));
    }

    @Test
    public void testMovDocumentoSuccess() {
        contentControlAlfresco.movDocumento(conexion.getSession(), mensajeRespuesta.getDocumentoDTOList().get(0).getIdDocumento(), "Comunicaciones Oficiales Externas Recibidas 2018", "1000.1040_GERENCIA NACIONAL DE GESTION DOCUMENTAL");
    }

    @Test
    public void testGetUDFolderByIdSuccess() {
        try {
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                    crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());

            UnidadDocumentalDTO unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            //Obtener la unidad documental

            final Optional<Folder> optionalDocumentalDTO = contentControlAlfresco.
                    getUDFolderById(unidadDocumentalDTO.getId(), conexion.getSession());

            optionalDocumentalDTO.ifPresent(unidadDocumentalDTO1 ->
                    assertNotNull(unidadDocumentalDTO1.getId()));

            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession());

        } catch (Exception e) {
            logger.error("Error: {}", e);
        }
    }

    @Test
    public void testActualizarUnidadDocumentalSuccess() {
        try {
            //Se crea la Unidad Documental
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                    crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());

            UnidadDocumentalDTO unidadDocumentalDTOInsertada = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            //Modificar el valor de la UD
            unidadDocumentalDTOInsertada.setNombreUnidadDocumental("OtroNombreParaProbar");
            //Verifico si se hizo la modificacion
            assertTrue(contentControlAlfresco.actualizarUnidadDocumental(unidadDocumentalDTOInsertada, conexion.getSession()));

            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession());

        } catch (SystemException e) {
            logger.error("Error: {}", e);
        } catch (Exception e) {
            logger.error("Error actualizando la UD: {}", e);
        }
    }

    @Test
    public void testObtenerDocumentosAdjuntosSuccess() {
        //Probar que sube documento EE correctemante
        //Adicionar como documento hijo del documento de prueba principal
        documentoDTO1.setIdDocumentoPadre(mensajeRespuesta.getDocumentoDTOList().get(0).getIdDocumento());
        mensajeRespuesta1 = contentControlAlfresco.subirDocumentoPrincipalAdjunto(conexion.getSession(), documentoDTO1, "EE");
        assertNotNull(contentControlAlfresco.obtenerDocumentosAdjuntos(conexion.getSession(), documentoDTO).getDocumentoDTOList());
        contentControlAlfresco.eliminardocumento(documentoDTO1.getIdDocumento(), conexion.getSession());

    }

    @Test
    public void testSubirVersionarDocumentoGeneradoSuccess() {
        //Probar obtener Versiones
        //Adicionar documento para version
        assertEquals("0000", contentControlAlfresco.subirVersionarDocumentoGenerado(conexion.getSession(), documentoDTO, "EE").getCodMensaje());

        //Eliminar la version del documento
        contentControlAlfresco.eliminardocumento(documentoDTO.getIdDocumento(), conexion.getSession());
    }

    @Test
    public void testSubirVersionarDocumentoGeneradoNuevoSuccess() {
        DocumentoDTO documentoDTO4 = new DocumentoDTO();
        documentoDTO4.setTipoDocumento("application/pdf");
        documentoDTO4.setNombreDocumento("testSubirVersionarDocumentoGeneradoNuevoSuccess");
        documentoDTO4.setVersionLabel("1.0");
        documentoDTO4.setNombreRemitente("UserTest");
        documentoDTO4.setNroRadicado("1234567");
        documentoDTO4.setTipologiaDocumental("Principal");
        String documento = "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u78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4KPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNC4yLjEtYzA0MyA1Mi4zNzI3MjgsIDIwMDkvMDEvMTgtMTU6MDg6MDQgICAgICAgICI+CiAgIDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+CiAgICAgIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiCiAgICAgICAgICAgIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyI+CiAgICAgICAgIDxkYzpmb3JtYXQ+YXBwbGljYXRpb24vcGRmPC9kYzpmb3JtYXQ+CiAgICAgICAgIDxkYzpjcmVhdG9yPgogICAgICAgICAgICA8cmRmOlNlcT4KICAgICAgICAgICAgICAgPHJkZjpsaT5jZGFpbHk8L3JkZjpsaT4KICAgICAgICAgICAgPC9yZGY6U2VxPgogICAgICAgICA8L2RjOmNyZWF0b3I+CiAgICAgICAgIDxkYzp0aXRsZT4KICAgICAgICAgICAgPHJkZjpBbHQ+CiAgICAgICAgICAgICAgIDxyZGY6bGkgeG1sOmxhbmc9IngtZGVmYXVsdCI+VGhpcyBpcyBhIHRlc3QgUERGIGZpbGU8L3JkZjpsaT4KICAgICAgICAgICAgPC9yZGY6QWx0PgogICAgICAgICA8L2RjOnRpdGxlPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgICAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgICAgICAgICAgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIj4KICAgICAgICAgPHhtcDpDcmVhdGVEYXRlPjIwMDAtMDYtMjlUMTA6MjE6MDgrMTE6MDA8L3htcDpDcmVhdGVEYXRlPgogICAgICAgICA8eG1wOkNyZWF0b3JUb29sPk1pY3Jvc29mdCBXb3JkIDguMDwveG1wOkNyZWF0b3JUb29sPgogICAgICAgICA8eG1wOk1vZGlmeURhdGU+MjAxMy0xMC0yOFQxNToyNDoxMy0wNDowMDwveG1wOk1vZGlmeURhdGU+CiAgICAgICAgIDx4bXA6TWV0YWRhdGFEYXRlPjIwMTMtMTAtMjhUMTU6MjQ6MTMtMDQ6MDA8L3htcDpNZXRhZGF0YURhdGU+CiAgICAgIDwvcmRmOkRlc2NyaXB0aW9uPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczpwZGY9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGRmLzEuMy8iPgogICAgICAgICA8cGRmOlByb2R1Y2VyPkFjcm9iYXQgRGlzdGlsbGVyIDQuMCBmb3IgV2luZG93czwvcGRmOlByb2R1Y2VyPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgICAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgICAgICAgICAgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iPgogICAgICAgICA8eG1wTU06RG9jdW1lbnRJRD51dWlkOjA4MDVlMjIxLTgwYTgtNDU5ZS1hNTIyLTYzNWVkNWMxZTJlNjwveG1wTU06RG9jdW1lbnRJRD4KICAgICAgICAgPHhtcE1NOkluc3RhbmNlSUQ+dXVpZDo2MmQ2YWU2ZC00M2M0LTQ3MmQtOWIyOC03YzRhZGQ4ZjllNDY8L3htcE1NOkluc3RhbmNlSUQ+CiAgICAgIDwvcmRmOkRlc2NyaXB0aW9uPgogICA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgCjw/eHBhY2tldCBlbmQ9InciPz4NCmVuZHN0cmVhbQ1lbmRvYmoNMiAwIG9iag08PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgNC9MZW5ndGggNDgvTiAxL1R5cGUvT2JqU3RtPj5zdHJlYW0NCmjeMlUwULCx0XfOL80rUTDU985MKY62BIoFxeqHVBak6gckpqcW29kBBBgA1ncLgA0KZW5kc3RyZWFtDWVuZG9iag0zIDAgb2JqDTw8L0ZpbHRlci9GbGF0ZURlY29kZS9GaXJzdCA0L0xlbmd0aCAxNjcvTiAxL1R5cGUvT2JqU3RtPj5zdHJlYW0NCmjePMvBCsIwEEXRX5mdDaKdxCpVSqFY3AkuBNexSelA6EAyRfx7A4qPu3znAAhNU3aLTByLwVkKb1Weo7dCPPdWfNGfDOYdzFGj0VivtV4hrn6vrK40RE48Cjw4Oqi3qMoruz/WuwxrvTeV3m2w+uJbZLcMPhZdxk8r0FMSCsFHqLYII0d40Oz4lVR5Jwm+uE+UIGdBfBK49RcYKXjVth8BBgBnZztkDQplbmRzdHJlYW0NZW5kb2JqDTQgMCBvYmoNPDwvRGVjb2RlUGFybXM8PC9Db2x1bW5zIDMvUHJlZGljdG9yIDEyPj4vRmlsdGVyL0ZsYXRlRGVjb2RlL0lEWzw0REM5MUExODc1QTZENzA3QUVDMjAzQkIwMjFDOTNBMD48RjZDOTJCMzY4QThBMTM0MDg0NTdBMUQzOTVBMzdFQjk+XS9JbmZvIDYgMCBSL0xlbmd0aCAzNy9Sb290IDggMCBSL1NpemUgNy9UeXBlL1hSZWYvV1sxIDIgMF0+PnN0cmVhbQ0KaN5iYmBgYGLkPcLEwD+ViYGhh4mBkYWJ8bEkkM0IEGAAKlkDFA0KZW5kc3RyZWFtDWVuZG9iag1zdGFydHhyZWYNCjExNg0KJSVFT0YNCg==";
        documentoDTO4.setDocumento(documento.getBytes());
        documentoDTO4.setSede("1000_VICEPRESIDENCIA ADMINISTRATIVA");
        documentoDTO4.setDependencia("1000.1040_GERENCIA NACIONAL DE GESTION DOCUMENTAL");
        documentoDTO4.setCodigoSede("1000");
        documentoDTO4.setCodigoDependencia("10001040");
        assertEquals("0000", contentControlAlfresco.subirVersionarDocumentoGenerado(conexion.getSession(), documentoDTO4, "EE").getCodMensaje());

        //Eliminar la version del documento
        contentControlAlfresco.eliminardocumento(documentoDTO4.getIdDocumento(), conexion.getSession());
    }

    @Test
    public void testObtenerVersionesDocumentoSuccess() {
        //Probar obtener Versiones
        //Adicionar documento para version
        mensajeRespuesta1 = contentControlAlfresco.subirVersionarDocumentoGenerado(conexion.getSession(), documentoDTO, "EE");

        //Obtener Versiones de documento
        assertEquals("0000", contentControlAlfresco.obtenerVersionesDocumento(conexion.getSession(), documentoDTO.getIdDocumento()).getCodMensaje());
        //Eliminar la version del documento
        contentControlAlfresco.eliminardocumento(documentoDTO.getIdDocumento(), conexion.getSession());
    }

    @Test
    public void testModificarMetadatosDocumentoFail() {
        DocumentoDTO documentoDTO12 = new DocumentoDTO();
        documentoDTO12.setIdDocumento("qwqwqwqw");
        assertEquals("00006", contentControlAlfresco.modificarMetadatosDocumento(conexion.getSession(), documentoDTO12.getIdDocumento(), "sdsdsd", documentoDTO.getTipologiaDocumental(), "Urbino").getCodMensaje());
    }

    @Test
    public void testModificarMetadatosDocumentoSuccess() {

        assertEquals("0000", contentControlAlfresco.modificarMetadatosDocumento(conexion.getSession(), documentoDTO.getIdDocumento(), "sdsdsd", documentoDTO.getTipologiaDocumental(), "Urbino").getCodMensaje());
    }

    @Test
    public void testObtenerPropiedadesDocumentoSuccess() {
        CmisObject cmisObjectDocument = conexion.getSession().getObject(documentoDTO.getIdDocumento());
        assertNotNull(contentControlAlfresco.obtenerPropiedadesDocumento((Document) cmisObjectDocument));
    }

    @Test
    public void testObtenerConexionSuccess() {

        assertNotNull(contentControlAlfresco.obtenerConexion());
    }

    @Test
    public void testGetDocumentosPorArchivarSuccess() throws Exception {
        assertEquals("0000", contentControlAlfresco.getDocumentosPorArchivar("10001040", conexion.getSession()).getCodMensaje());
    }

    @Test
    public void testObtenerDocumentosArchivadosSuccess() {
        try {
            assertEquals("0000", contentControlAlfresco.obtenerDocumentosArchivados("10001040", conexion.getSession()).getCodMensaje());
            contentControlAlfresco.obtenerDocumentosArchivados("", conexion.getSession());
        } catch (Exception e) {
            assertEquals("No se ha especificado el codigo de la dependencia", e.getMessage());
        }

    }

    @Test
    public void testSubirDocumentosTemporalesUDSuccess() {
        ArrayList<DocumentoDTO> listaDocs = new ArrayList();
        listaDocs.add(documentoDTO);
        listaDocs.add(documentoDTO1);

        try {
            assertEquals("0000", contentControlAlfresco.subirDocumentosTemporalesUD(listaDocs, conexion.getSession()).getCodMensaje());
            contentControlAlfresco.eliminardocumento(documentoDTO.getIdDocumento(), conexion.getSession());
            contentControlAlfresco.eliminardocumento(documentoDTO1.getIdDocumento(), conexion.getSession());

            ArrayList<DocumentoDTO> listaDocs1 = new ArrayList();
            contentControlAlfresco.subirDocumentosTemporalesUD(listaDocs1, conexion.getSession());
        } catch (Exception e) {
            assertEquals("La lista de documentos esta vacia", e.getMessage());
        }

    }

    @Test
    public void testGetDocumentsFromFolderSuccess() {
        try {
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());

            UnidadDocumentalDTO unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");

            //Obtener la unidad documental
            final Optional<Folder> optionalFolder = contentControlAlfresco.
                    getUDFolderById(unidadDocumentalDTO.getId(), conexion.getSession());

            Map<String, Object> properties = new HashMap<>();
            properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
            properties.put(PropertyIds.NAME, "Doc Pruba");

            ContentStream contentStream = new ContentStreamImpl(documentoDTO.getNombreDocumento(), BigInteger.valueOf(documentoDTO.getDocumento().length), documentoDTO.getTipoDocumento(), new ByteArrayInputStream(documentoDTO.getDocumento()
            ));

            optionalFolder.ifPresent(optionalFolder1 -> optionalFolder1.createDocument(properties, contentStream, VersioningState.MAJOR));

            optionalFolder.ifPresent(optionalFolder1 -> {
                try {
                    assertNotNull(contentControlAlfresco.getDocumentsFromFolder(optionalFolder1).get(0).getIdDocumento());
                } catch (SystemException e) {
                    e.printStackTrace();
                }
            });

            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession());

        } catch (SystemException e) {
            logger.error("Error: {}", e);
        } catch (Exception e) {
            logger.error("Error: {}", e);
        }

    }

    @Test
    public void testSubirDocumentosUnidadDocumentalECMSuccess() {
        try {
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());

            UnidadDocumentalDTO unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            List<DocumentoDTO> documentoDTOS = new ArrayList<>();
            documentoDTOS.add(documentoDTO);
            unidadDocumentalDTO.setListaDocumentos(documentoDTOS);
            assertEquals("0000", contentControlAlfresco.subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO, conexion.getSession()).getCodMensaje());
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModificarUnidadesDocumentalesSuccess() {


        List<UnidadDocumentalDTO> unidadDocumentalDTOList = new ArrayList<>();

        try {

            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());

            UnidadDocumentalDTO unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            unidadDocumentalDTO.setNombreUnidadDocumental("DepPruebaModificarUnidadesDocumentales");
            unidadDocumentalDTOList.add(unidadDocumentalDTO);

            assertEquals("0000", contentControlAlfresco.modificarUnidadesDocumentales(unidadDocumentalDTOList, conexion.getSession()).getCodMensaje());
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession());
        } catch (SystemException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testModificarUnidadesDocumentalesFail() {


        List<UnidadDocumentalDTO> unidadDocumentalDTOList = new ArrayList<>();

        try {
            assertEquals("0000", contentControlAlfresco.modificarUnidadesDocumentales(unidadDocumentalDTOList, conexion.getSession()).getCodMensaje());
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession());
        } catch (SystemException e) {
            assertEquals("No se ha introducido la unidad documental a modificar",e.getReason());
        }
    }

    @Test
    public void testEliminarUnidadDocumentalSuccess() {
        MensajeRespuesta mensajeRespuesta = null;
        try {
            unidadDocumentalDTO.setId("12121212");
            mensajeRespuesta = contentControlAlfresco.crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());

            UnidadDocumentalDTO unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            assertEquals("0000", contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession()).getCodMensaje());
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEliminarUnidadDocumentalFail() {
        try {
            UnidadDocumentalDTO unidadDocumentalDTOFail = new UnidadDocumentalDTO();
            unidadDocumentalDTOFail.setId("NoId");

            assertEquals("1224", contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTOFail.getId(), conexion.getSession()).getCodMensaje());
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void crearLinkDocumentosApoyo() {
    }


}