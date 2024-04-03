package grillo78.beycraft.common.particle;

import grillo78.beycraft.Beycraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Beycraft.MOD_ID);

    public static BasicParticleType SPARKLE = register("sparkle", new BasicParticleType(false));
    public static BasicParticleType RESONANCE = register("resonance", new BasicParticleType(false));

    private static <T extends ParticleType> T register(String name, T particle) {
        PARTICLES.register(name, () -> particle);
        return particle;
    }
}
