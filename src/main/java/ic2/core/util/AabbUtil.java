package ic2.core.util;

import ic2.api.Direction;
import ic2.core.util.AabbUtil$1;
import ic2.core.util.AabbUtil$Edge;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class AabbUtil
{
    public static Direction getIntersection(Vec3 origin, Vec3 direction, AxisAlignedBB bbox, Vec3 intersection)
    {
        double length = direction.lengthVector();
        Vec3 normalizedDirection = Vec3.createVectorHelper(direction.xCoord / length, direction.yCoord / length, direction.zCoord / length);
        Direction intersectingDirection = intersects(origin, normalizedDirection, bbox);

        if (intersectingDirection == null)
        {
            return null;
        }
        else
        {
            Vec3 planeOrigin;

            if (normalizedDirection.xCoord < 0.0D && normalizedDirection.yCoord < 0.0D && normalizedDirection.zCoord < 0.0D)
            {
                planeOrigin = Vec3.createVectorHelper(bbox.maxX, bbox.maxY, bbox.maxZ);
            }
            else if (normalizedDirection.xCoord < 0.0D && normalizedDirection.yCoord < 0.0D && normalizedDirection.zCoord >= 0.0D)
            {
                planeOrigin = Vec3.createVectorHelper(bbox.maxX, bbox.maxY, bbox.minZ);
            }
            else if (normalizedDirection.xCoord < 0.0D && normalizedDirection.yCoord >= 0.0D && normalizedDirection.zCoord < 0.0D)
            {
                planeOrigin = Vec3.createVectorHelper(bbox.maxX, bbox.minY, bbox.maxZ);
            }
            else if (normalizedDirection.xCoord < 0.0D && normalizedDirection.yCoord >= 0.0D && normalizedDirection.zCoord >= 0.0D)
            {
                planeOrigin = Vec3.createVectorHelper(bbox.maxX, bbox.minY, bbox.minZ);
            }
            else if (normalizedDirection.xCoord >= 0.0D && normalizedDirection.yCoord < 0.0D && normalizedDirection.zCoord < 0.0D)
            {
                planeOrigin = Vec3.createVectorHelper(bbox.minX, bbox.maxY, bbox.maxZ);
            }
            else if (normalizedDirection.xCoord >= 0.0D && normalizedDirection.yCoord < 0.0D && normalizedDirection.zCoord >= 0.0D)
            {
                planeOrigin = Vec3.createVectorHelper(bbox.minX, bbox.maxY, bbox.minZ);
            }
            else if (normalizedDirection.xCoord >= 0.0D && normalizedDirection.yCoord >= 0.0D && normalizedDirection.zCoord < 0.0D)
            {
                planeOrigin = Vec3.createVectorHelper(bbox.minX, bbox.minY, bbox.maxZ);
            }
            else
            {
                planeOrigin = Vec3.createVectorHelper(bbox.minX, bbox.minY, bbox.minZ);
            }

            Vec3 planeNormalVector = null;

            switch (AabbUtil$1.$SwitchMap$ic2$api$Direction[intersectingDirection.ordinal()])
            {
                case 1:
                case 2:
                    planeNormalVector = Vec3.createVectorHelper(1.0D, 0.0D, 0.0D);
                    break;

                case 3:
                case 4:
                    planeNormalVector = Vec3.createVectorHelper(0.0D, 1.0D, 0.0D);
                    break;

                case 5:
                case 6:
                    planeNormalVector = Vec3.createVectorHelper(0.0D, 0.0D, 1.0D);
            }

            Vec3 newIntersection = getIntersectionWithPlane(origin, normalizedDirection, planeOrigin, planeNormalVector);
            intersection.xCoord = newIntersection.xCoord;
            intersection.yCoord = newIntersection.yCoord;
            intersection.zCoord = newIntersection.zCoord;
            return intersectingDirection;
        }
    }

    public static Direction intersects(Vec3 origin, Vec3 direction, AxisAlignedBB bbox)
    {
        double[] ray = getRay(origin, direction);
        return direction.xCoord < 0.0D && direction.yCoord < 0.0D && direction.zCoord < 0.0D ? (origin.xCoord < bbox.minX ? null : (origin.yCoord < bbox.minY ? null : (origin.zCoord < bbox.minZ ? null : (side(ray, getEdgeRay(AabbUtil$Edge.EF, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.EH, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.DH, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.DC, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.BC, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.BF, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.HG, bbox)) > 0.0D && side(ray, getEdgeRay(AabbUtil$Edge.FG, bbox)) < 0.0D ? Direction.ZP : (side(ray, getEdgeRay(AabbUtil$Edge.CG, bbox)) < 0.0D ? Direction.YP : Direction.XP))))))))))) : (direction.xCoord < 0.0D && direction.yCoord < 0.0D && direction.zCoord >= 0.0D ? (origin.xCoord < bbox.minX ? null : (origin.yCoord < bbox.minY ? null : (origin.zCoord > bbox.maxZ ? null : (side(ray, getEdgeRay(AabbUtil$Edge.HG, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.DH, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AD, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AB, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.BF, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.FG, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.DC, bbox)) > 0.0D && side(ray, getEdgeRay(AabbUtil$Edge.CG, bbox)) > 0.0D ? Direction.XP : (side(ray, getEdgeRay(AabbUtil$Edge.BC, bbox)) < 0.0D ? Direction.YP : Direction.ZN))))))))))) : (direction.xCoord < 0.0D && direction.yCoord >= 0.0D && direction.zCoord < 0.0D ? (origin.xCoord < bbox.minX ? null : (origin.yCoord > bbox.maxY ? null : (origin.zCoord < bbox.minZ ? null : (side(ray, getEdgeRay(AabbUtil$Edge.FG, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.EF, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AE, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AD, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.DC, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.CG, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.EH, bbox)) > 0.0D && side(ray, getEdgeRay(AabbUtil$Edge.HG, bbox)) > 0.0D ? Direction.ZP : (side(ray, getEdgeRay(AabbUtil$Edge.DH, bbox)) < 0.0D ? Direction.XP : Direction.YN))))))))))) : (direction.xCoord < 0.0D && direction.yCoord >= 0.0D && direction.zCoord >= 0.0D ? (origin.xCoord < bbox.minX ? null : (origin.yCoord > bbox.maxY ? null : (origin.zCoord > bbox.maxZ ? null : (side(ray, getEdgeRay(AabbUtil$Edge.EH, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AE, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AB, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.BC, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.CG, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.HG, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AD, bbox)) > 0.0D && side(ray, getEdgeRay(AabbUtil$Edge.DH, bbox)) > 0.0D ? Direction.YN : (side(ray, getEdgeRay(AabbUtil$Edge.DC, bbox)) < 0.0D ? Direction.ZN : Direction.XP))))))))))) : (direction.xCoord >= 0.0D && direction.yCoord < 0.0D && direction.zCoord < 0.0D ? (origin.xCoord > bbox.maxX ? null : (origin.yCoord < bbox.minY ? null : (origin.zCoord < bbox.minZ ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AB, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AE, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.EH, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.HG, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.CG, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.BC, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.EF, bbox)) > 0.0D && side(ray, getEdgeRay(AabbUtil$Edge.BF, bbox)) < 0.0D ? Direction.XN : (side(ray, getEdgeRay(AabbUtil$Edge.FG, bbox)) < 0.0D ? Direction.ZP : Direction.YP))))))))))) : (direction.xCoord >= 0.0D && direction.yCoord < 0.0D && direction.zCoord >= 0.0D ? (origin.xCoord > bbox.maxX ? null : (origin.yCoord < bbox.minY ? null : (origin.zCoord > bbox.maxZ ? null : (side(ray, getEdgeRay(AabbUtil$Edge.DC, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AD, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AE, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.EF, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.FG, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.CG, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AB, bbox)) > 0.0D && side(ray, getEdgeRay(AabbUtil$Edge.BC, bbox)) > 0.0D ? Direction.ZN : (side(ray, getEdgeRay(AabbUtil$Edge.BF, bbox)) < 0.0D ? Direction.XN : Direction.YP))))))))))) : (direction.xCoord >= 0.0D && direction.yCoord >= 0.0D && direction.zCoord < 0.0D ? (origin.xCoord > bbox.maxX ? null : (origin.yCoord > bbox.maxY ? null : (origin.zCoord < bbox.minZ ? null : (side(ray, getEdgeRay(AabbUtil$Edge.BF, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AB, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AD, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.DH, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.HG, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.FG, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AE, bbox)) > 0.0D && side(ray, getEdgeRay(AabbUtil$Edge.EF, bbox)) > 0.0D ? Direction.XN : (side(ray, getEdgeRay(AabbUtil$Edge.EH, bbox)) < 0.0D ? Direction.YN : Direction.ZP))))))))))) : (origin.xCoord > bbox.maxX ? null : (origin.yCoord > bbox.maxY ? null : (origin.zCoord > bbox.maxZ ? null : (side(ray, getEdgeRay(AabbUtil$Edge.EF, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.EH, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.DH, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.DC, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.BC, bbox)) < 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.BF, bbox)) > 0.0D ? null : (side(ray, getEdgeRay(AabbUtil$Edge.AB, bbox)) < 0.0D && side(ray, getEdgeRay(AabbUtil$Edge.AE, bbox)) > 0.0D ? Direction.XN : (side(ray, getEdgeRay(AabbUtil$Edge.AD, bbox)) < 0.0D ? Direction.ZN : Direction.YN)))))))))))))))));
    }

    private static double[] getRay(Vec3 origin, Vec3 direction)
    {
        double[] ret = new double[] {origin.xCoord * direction.yCoord - direction.xCoord * origin.yCoord, origin.xCoord * direction.zCoord - direction.xCoord * origin.zCoord, -direction.xCoord, origin.yCoord * direction.zCoord - direction.yCoord * origin.zCoord, -direction.zCoord, direction.yCoord};
        return ret;
    }

    private static double[] getEdgeRay(AabbUtil$Edge edge, AxisAlignedBB bbox)
    {
        switch (AabbUtil$1.$SwitchMap$ic2$core$util$AabbUtil$Edge[edge.ordinal()])
        {
            case 1:
                return new double[] { -bbox.minY, -bbox.minZ, -1.0D, 0.0D, 0.0D, 0.0D};
            case 2:
                return new double[] {bbox.minX, 0.0D, 0.0D, -bbox.minZ, 0.0D, 1.0D};
            case 3:
                return new double[] {0.0D, bbox.minX, 0.0D, bbox.minY, -1.0D, 0.0D};
            case 4:
                return new double[] {bbox.maxX, 0.0D, 0.0D, -bbox.minZ, 0.0D, 1.0D};
            case 5:
                return new double[] {0.0D, bbox.maxX, 0.0D, bbox.minY, -1.0D, 0.0D};
            case 6:
                return new double[] { -bbox.maxY, -bbox.minZ, -1.0D, 0.0D, 0.0D, 0.0D};
            case 7:
                return new double[] {0.0D, bbox.minX, 0.0D, bbox.maxY, -1.0D, 0.0D};
            case 8:
                return new double[] { -bbox.minY, -bbox.maxZ, -1.0D, 0.0D, 0.0D, 0.0D};
            case 9:
                return new double[] {bbox.minX, 0.0D, 0.0D, -bbox.maxZ, 0.0D, 1.0D};
            case 10:
                return new double[] {0.0D, bbox.maxX, 0.0D, bbox.maxY, -1.0D, 0.0D};
            case 11:
                return new double[] { -bbox.maxY, -bbox.maxZ, -1.0D, 0.0D, 0.0D, 0.0D};
            case 12:
                return new double[] {bbox.maxX, 0.0D, 0.0D, -bbox.maxZ, 0.0D, 1.0D};
            default:
                return new double[0];
        }
    }

    private static double side(double[] ray1, double[] ray2)
    {
        return ray1[2] * ray2[3] + ray1[5] * ray2[1] + ray1[4] * ray2[0] + ray1[1] * ray2[5] + ray1[0] * ray2[4] + ray1[3] * ray2[2];
    }

    private static Vec3 getIntersectionWithPlane(Vec3 origin, Vec3 direction, Vec3 planeOrigin, Vec3 planeNormalVector)
    {
        double distance = getDistanceToPlane(origin, direction, planeOrigin, planeNormalVector);
        return Vec3.createVectorHelper(origin.xCoord + direction.xCoord * distance, origin.yCoord + direction.yCoord * distance, origin.zCoord + direction.zCoord * distance);
    }

    private static double getDistanceToPlane(Vec3 origin, Vec3 direction, Vec3 planeOrigin, Vec3 planeNormalVector)
    {
        Vec3 base = Vec3.createVectorHelper(planeOrigin.xCoord - origin.xCoord, planeOrigin.yCoord - origin.yCoord, planeOrigin.zCoord - origin.zCoord);
        return dotProduct(base, planeNormalVector) / dotProduct(direction, planeNormalVector);
    }

    private static double dotProduct(Vec3 a, Vec3 b)
    {
        return a.xCoord * b.xCoord + a.yCoord * b.yCoord + a.zCoord * b.zCoord;
    }
}
